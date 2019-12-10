package com.fengchao.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.sso.bean.*;
import com.fengchao.sso.config.SSOConfiguration;
import com.fengchao.sso.dao.BalanceDao;
import com.fengchao.sso.dao.BindSubAccountDao;
import com.fengchao.sso.dao.UserDao;
import com.fengchao.sso.feign.*;
import com.fengchao.sso.mapper.*;
import com.fengchao.sso.mapper.custom.LoginCustomMapper;
import com.fengchao.sso.model.*;
import com.fengchao.sso.service.ILoginService;
import com.fengchao.sso.util.*;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@EnableConfigurationProperties({SSOConfiguration.class})
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private LoginCustomMapper loginCustomMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private PinganClientService pinganClientService;
    @Autowired
    private GuanaitongClientService guanaitongClientService ;
    @Autowired
    private OrderServiceClient orderService;
    @Autowired
    private EquityService equityService;
    @Autowired
    private RedisDAO redisDAO ;
    @Autowired
    private BalanceDao balanceDao;
    @Autowired
    private ProductService productService ;
    @Autowired
    private BaseService baseService ;
    @Autowired
    private UserDao userDao ;
    @Autowired
    private SSOConfiguration ssoConfiguration;
    @Autowired
    private BindSubAccountDao bindSubAccountDao ;
    @Autowired
    private BindSubAccountMapper bindSubAccountMapper ;
    @Autowired
    private SUserMapper mapper ;

    @Override
    public Login selectByPrimaryName(String username) {
        return loginMapper.selectByPrimaryKey(username);
    }

    @Override
    public int insertSelective(LoginBean loginBean) {
        Login login=new Login();
        User user = new User();
        login.setUsername(loginBean.getUsername());
        login.setPassword(loginBean.getPassword());
        login.setCreatdate(new Date());
        int insertNum = loginMapper.insertSelective(login);
        user.setLoginId(Integer.parseInt(login.getId()));
        return userMapper.insertSelective(user);
    }

    @Override
    public int updatePassword(LoginBean loginBean) {
        Login login=new Login();
        login.setUsername(loginBean.getUsername());
        login.setPassword(loginBean.getPassword());
        return loginMapper.updatePassword(login);
    }

    @Override
    public void updatePasswordByUsername(String username, String newPassword) {
        Login login=new Login();
        login.setUsername(username);
        login.setPassword(newPassword);
        loginCustomMapper.updatePasswordByUsername(login);
    }

    @Override
    public TokenBean thirdLogin(ThirdLoginBean loginBean) {
        log.info("第三方登录，入参：{}", JSONUtil.toJsonString(loginBean));
        String token = JwtTokenUtil.generateToken(loginBean);
        log.info("第三方登录，token ：{}", token);
        TokenBean bean = new TokenBean();
        bean.setToken(token);
        bean.setOpenId(loginBean.getOpenId());
        bean.setThirdToken(loginBean.getAccessToken());
        bean.setExpireDate(JwtTokenUtil.EXPIRATIONTIME);
        bean.setNewUser(true);
        // 验证是否为新用户
        TokenBean verifyBean =  verifyNewUser(loginBean) ;
        bean.setNewUser(verifyBean.isNewUser());

        Token tokenBean = new Token();
        tokenBean.setToken(bean.getToken());
        tokenBean.setOpenId(bean.getOpenId());
        tokenBean.setThirdToken(bean.getThirdToken());
        tokenBean.setExpireDate(bean.getExpireDate());
        Token temp = tokenMapper.selectByOpenId(loginBean.getOpenId());
        if (temp != null) {
            tokenBean.setUpdatedAt(new Date());
            tokenMapper.updateByPrimaryKey(tokenBean);
        } else {
            tokenBean.setCreatedAt(new Date());
            tokenMapper.insertSelective(tokenBean);
            User tempU = new User();
            tempU.setOpenId(loginBean.getOpenId());
            tempU.setiAppId(loginBean.getiAppId());
            User user = userMapper.selectByOpenId(tempU);
            if (user == null) {
                bean.setNewUser(true);
                user = new User();
                user.setOpenId(loginBean.getOpenId());
                String nickname = "fc_" + user.getOpenId().substring(user.getOpenId().length() - 8);
                user.setNickname(nickname);
                user.setCreatedAt(new Date());
                user.setiAppId(loginBean.getiAppId());
                userMapper.insertSelective(user);
            }
        }
        redisDAO.setKey("sso:" + loginBean.getiAppId() + loginBean.getOpenId(), token, JwtTokenUtil.EXPIRATIONTIME);
        log.info("第三方登录，返回结果 ：{}", JSONUtil.toJsonString(bean));
        return bean;
    }

    private TokenBean verifyNewUser(ThirdLoginBean loginBean) {
        TokenBean bean = new TokenBean() ;
        bean.setNewUser(true);
        // 查询订单
        OperaResult orderResult = orderService.findOrderListByOpenId(loginBean.getiAppId() + loginBean.getOpenId());
        if (orderResult.getCode() == 200) {
            Map<String, Object> data = orderResult.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            if(orders != null && orders.size() > 0){
                bean.setNewUser(false);
                return bean;
            }
        }
        // 查询优惠券
        OperaResult couponResult = equityService.findCollectGiftCouponByOpenId(loginBean.getiAppId() + loginBean.getOpenId());
        if (couponResult.getCode() == 200) {
            Map<String, Object> data = couponResult.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Coupon> coupons = JSONObject.parseArray(jsonString, Coupon.class) ;
            if(coupons != null && coupons.size() > 0){
                bean.setNewUser(false);
                return bean ;
            }
        }
        return bean;
    }

    @Override
    public OperaResult findThirdPartyToken(String iAppId, String initCode) {
        OperaResult result = new OperaResult();
        AccessToken accessToken = new AccessToken() ;
        accessToken = getPingAnToken(initCode) ;
        result.getData().put("result", accessToken);
        return result ;
    }

    @Override
    public OperaResult findThirdPartyTokenGAT(String iAppId, String initCode) {
        OperaResult result = new OperaResult();
        AccessToken accessToken = new AccessToken() ;
        // 获取关爱通登录信息
        OpenId openId = getGuanaitongOpenId(initCode) ;
        if (openId ==null || openId.getOpen_id() == null || "".equals(openId.getOpen_id())) {
            result.setCode(9000001);
            result.setMsg("关爱通获取openId失败。");
            return  result;
        }
        accessToken.setOpenId(openId.getOpen_id());
        User temp = new User();
        temp.setOpenId(openId.getOpen_id());
        temp.setiAppId(iAppId);
        User user = userMapper.selectByOpenId(temp);
        if (user == null) {
            GuanaitongUserBean guanaitongUserBean = getGuanaitongUser(openId.getOpen_id()) ;
            user = new User();
            user.setOpenId(openId.getOpen_id());
            if (!StringUtils.isEmpty(guanaitongUserBean.getName())) {
                user.setNickname(guanaitongUserBean.getName());
            } else {
                String nickname = "fc_" + guanaitongUserBean.getOpen_id().substring(user.getOpenId().length() - 8);
                user.setNickname(nickname);
            }
            user.setName(guanaitongUserBean.getName());
            user.setTelephone(guanaitongUserBean.getMobile());
            user.setCreatedAt(new Date());
            user.setiAppId(iAppId);
            userMapper.insertSelective(user);
        }
        result.getData().put("result", accessToken);
        return result ;
    }

    @Override
    public OperaResult getPingAnOpenId(String iAppId, String requestCode) {
        OperaResult result = new OperaResult();
        AccessToken accessToken = new AccessToken() ;
        // 获取平安用户信息
        OperaResponse<AuthUserBean> authUserBeanOperaResponse = pinganClientService.checkRequestCode(requestCode, iAppId) ;
        if (authUserBeanOperaResponse.getCode() != 200) {
            result.setCode(authUserBeanOperaResponse.getCode());
            result.setMsg(authUserBeanOperaResponse.getMsg());
            return result;
        }
        AuthUserBean authUserBean = authUserBeanOperaResponse.getData();
        if (authUserBean ==null || authUserBean.getOpenId() == null || "".equals(authUserBean.getOpenId())) {
            result.setCode(9000001);
            result.setMsg("获取openId失败。");
            return  result;
        }
        accessToken.setOpenId(authUserBean.getOpenId());
        accessToken.setPayId(authUserBean.getPayId());
        User temp = new User();
        temp.setOpenId(authUserBean.getOpenId());
        temp.setiAppId(iAppId);
        User user = userMapper.selectByOpenId(temp);
        if (user == null) {
            user = new User();
            user.setOpenId(authUserBean.getOpenId());
            if (!StringUtils.isEmpty(authUserBean.getNickName())) {
                user.setNickname(authUserBean.getNickName());
            } else {
                String nickname = "fc_" + authUserBean.getOpenId().substring(user.getOpenId().length() - 8);
                user.setNickname(nickname);
            }
            user.setTelephone(authUserBean.getMobileNo());
            user.setCreatedAt(new Date());
            user.setiAppId(iAppId);
            userMapper.insertSelective(user);
        }
        balanceDao.updateOpenIdByTel(authUserBean.getMobileNo(), authUserBean.getOpenId());
        result.getData().put("result", accessToken);
        log.info("Third party Token 返回值： {}", JSONUtil.toJsonString(result));
        return result ;
    }

    @Override
    public OperaResponse getWXOpenIdByAppIdAndCode(String appId, String code) {
        OperaResponse response = new OperaResponse() ;
        if (StringUtils.isEmpty(appId)) {
            response.setCode(900001);
            response.setMsg("appId不能为空");
            return response ;
        }
        if (StringUtils.isEmpty(code)) {
            response.setCode(900001);
            response.setMsg("code不能为空");
            return response ;
        }
//        Platform platform = productService.findPlatformByAppId(appId).getData() ;
//        if (platform == null) {
//            response.setCode(900002);
//            response.setMsg("appId不存在");
//            return response ;
//        }
        SSOConfigBean configBean = getSSOConfig(appId) ;
        String bean = HttpClient.get(configBean.getWxAppId(), configBean.getWxAppSecret(), code, String.class) ;
        log.info(bean);
        JSONObject jsonObject = JSON.parseObject(bean) ;
        if (bean == null) {
            response.setCode(900003);
            response.setMsg("获取微信openId失败");
            return response ;
        }
        String openId = jsonObject.getString("openid") ;
        if (StringUtils.isEmpty(openId)) {
            response.setCode(900003);
            response.setMsg("获取微信openId失败");
            return response ;
        }
//        String wxToken = jsonObject.getString("access_token") ;
        response.setData(jsonObject);
        return response;
    }

    @Override
    public OperaResponse verifyCode(String telephone, String type, String appId) {
        OperaResponse result = new OperaResponse() ;
        if(StringUtil.isEmpty(telephone)){
            result.setCode(100000);
            result.setMsg("电话号码不正确");
            return result;
        }
        SMSPostBean smsPostBean = new SMSPostBean() ;
        smsPostBean.setPhone(telephone);
        String code = baseService.send(smsPostBean).getData() ;
        redisDAO.setKey(type + ":sso:" + appId + telephone, code, 5 * 60 * 1000);
        return result;
    }

    @Override
    public OperaResponse bindWXOpenId(BindWXBean bandWXBean) {
        OperaResponse result = new OperaResponse() ;
        if(StringUtil.isEmpty(bandWXBean.getTelephone())){
            result.setCode(100000);
            result.setMsg("电话号码不能为空");
            return result;
        }
        if(StringUtil.isEmpty(bandWXBean.getOpenId())){
            result.setCode(100000);
            result.setMsg("openId不能为空");
            return result;
        }
        if(StringUtil.isEmpty(bandWXBean.getCode())){
            result.setCode(100000);
            result.setMsg("验证码不能为空");
            return result;
        }
        if(StringUtil.isEmpty(bandWXBean.getAppId())){
            result.setCode(100000);
            result.setMsg("appId不正确");
            return result;
        }
        // 根据子账号appId获取主账户appId
        OperaResponse<Platform> platformOperaResponse = productService.findPlatformBySubAppId(bandWXBean.getAppId()) ;
        Platform platform = new Platform() ;
        if (platformOperaResponse.getCode() == 200) {
            platform = platformOperaResponse.getData() ;
        }
        if (platform == null) {
            result.setCode(100000);
            result.setMsg("appId所属主账户不存在");
            return result;
        }
        String code = redisDAO.getValue("wx:sso:" + bandWXBean.getAppId() + bandWXBean.getTelephone()) ;
        if (bandWXBean.getCode().equals(code)) {
            // 绑定openId 查询手机号是否存在，绑定公众号

            SUser user = userDao.selectUserByTel(platform.getAppId(), bandWXBean.getTelephone()) ;
            if (user == null) {
                result.setCode(900100);
                result.setMsg("电话号码不正确或此用户不存在");
                return result;
            }
            // 绑定第三方openId
            BindSubAccount bindSubAccount = bindSubAccountDao.selectByUserIdAndAppId(bandWXBean.getAppId(), user.getId()) ;
            Date date = new Date() ;
            if (bindSubAccount == null) {
                bindSubAccount = new BindSubAccount() ;
                bindSubAccount.setCreatedAt(date);
                bindSubAccount.setUpdatedAt(date);
                bindSubAccount.setUserId(user.getId());
                bindSubAccount.setOpenId(bandWXBean.getOpenId());
                bindSubAccount.setAppId(bandWXBean.getAppId());
                // 添加
                bindSubAccountMapper.insertSelective(bindSubAccount) ;
            } else {
                // 修改
                bindSubAccount.setOpenId(bandWXBean.getOpenId());
                bindSubAccount.setUpdatedAt(date);
                bindSubAccountMapper.updateByPrimaryKeySelective(bindSubAccount) ;
            }
            result.setData(user);
        } else {
            result.setCode(100000);
            result.setMsg("验证码不正确");
            return result;
        }
        return result;
    }

    @Override
    public OperaResponse wxBindVerify(String appId, String openId) {
        OperaResponse result = new OperaResponse() ;
        if(StringUtil.isEmpty(openId)){
            result.setCode(100000);
            result.setMsg("openId不能为空");
            return result;
        }
        if(StringUtil.isEmpty(appId)){
            result.setCode(100000);
            result.setMsg("appId不正确");
            return result;
        }
        BindSubAccount bindSubAccount = bindSubAccountDao.selectByOpenIdAndAppId(appId, openId) ;
        result.setData(mapper.selectByPrimaryKey(bindSubAccount.getUserId()));
        return result;
    }

    private AccessToken getPingAnToken(String initCode) {
        OperaResult result = pinganClientService.findToken(initCode);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            AccessToken accessToken = JSONObject.parseObject(jsonString, AccessToken.class) ;
            return accessToken;
        }
        return null;
    }

    private OpenId getGuanaitongOpenId(String authCode) {
        AuthCode authCode1 = new AuthCode();
        authCode1.setAuth_code(authCode);
        Result result = guanaitongClientService.findOpenId(authCode1);
        if (result.getCode() == 200) {
            Object object = result.getData() ;
            String jsonString = JSON.toJSONString(object);
            OpenId openId = JSONObject.parseObject(jsonString, OpenId.class) ;
            return openId;
        }
        return null;
    }

    private GuanaitongUserBean getGuanaitongUser(String openId) {
        OpenId openId1 = new OpenId();
        openId1.setOpen_id(openId);
        Result result = guanaitongClientService.findUser(openId1);
        if (result.getCode() == 200) {
            Object object = result.getData() ;
            String jsonString = JSON.toJSONString(object);
            GuanaitongUserBean guanaitongUserBean = JSONObject.parseObject(jsonString, GuanaitongUserBean.class) ;
            return guanaitongUserBean;
        }
        return null;
    }

    private SSOConfigBean getSSOConfig(String appId) {
        return ssoConfiguration.getRegion().get(appId) ;
    }

    private String getSSOConfigByAppId(String appId) {
        Map<String, SSOConfigBean> map = ssoConfiguration.getRegion() ;
        for (String key : map.keySet()) {
            SSOConfigBean configBean = map.get(key);
            if (appId.equals(configBean.getGatAppId())) {
                return key ;
            }
        }
        return null ;
    }

}
