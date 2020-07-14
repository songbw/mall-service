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
import com.fengchao.sso.service.WeChatService;
import com.fengchao.sso.util.*;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private WeChatService weChatService ;

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
        login.setAppId(loginBean.getAppId());
        int insertNum = loginMapper.insertSelective(login);
        user.setLoginId(Integer.parseInt(login.getId()));
        user.setiAppId(loginBean.getAppId());
        user.setOpenId(Md5Util.md5(loginBean.getUsername() + login.getAppId()));
        String nickname = "fc_" + user.getOpenId().substring(user.getOpenId().length() - 8);
        user.setTelephone(loginBean.getUsername());
        user.setNickname(nickname);
        SUser verify = userDao.selectUserByAppIdAndOpenId(login.getAppId(), login.getUsername()) ;
        if (verify == null) {
            return userMapper.insertSelective(user);
        } else {
            return 0;
        }
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
        // 获取用户信息
        getUserInfo(loginBean) ;
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

    /**
     * 获取用户信息
     * @param loginBean
     */
    private void getUserInfo(ThirdLoginBean loginBean) {
        User tempU = new User();
        tempU.setOpenId(loginBean.getOpenId());
        tempU.setiAppId(loginBean.getiAppId());
        User user = userMapper.selectByOpenId(tempU);
        if (user == null) {
            if ("14".equals(loginBean.getiAppId())) {
                // 获取万科云城用户信息
                OperaResponse response = pinganClientService.findWKUser(loginBean.getOpenId(), loginBean.getAccessToken()) ;
                if (response.getCode() == 200) {
                    Object object = response.getData() ;
                    String jsonString = JSON.toJSONString(object);
                    JSONObject jsonObject = JSONObject.parseObject(jsonString, JSONObject.class) ;
                    user = new User() ;
                    user.setOpenId(loginBean.getOpenId());
                    user.setiAppId(loginBean.getiAppId());
                    if (StringUtils.isEmpty(jsonObject.getString("aliasName"))) {
                        String nickname = "fc_" + user.getOpenId().substring(user.getOpenId().length() - 8);
                        user.setNickname(nickname);
                    } else {
                        user.setNickname(jsonObject.getString("aliasName"));
                    }
                    user.setTelephone(jsonObject.getString("mobilePhone"));
                    user.setName(jsonObject.getString("userName"));
                    user.setHeadImg(jsonObject.getString("headIconUrl"));
                    userMapper.insertSelective(user);
                }
            }
        }

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
        OpenId openId = getGuanaitongOpenId(initCode, iAppId) ;
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
            GuanaitongUserBean guanaitongUserBean = getGuanaitongUser(openId.getOpen_id(), iAppId) ;
            SUser userByTel = userDao.selectUserByTel(iAppId, guanaitongUserBean.getMobile()) ;
            Date date = new Date() ;
            user = new User();
            BindSubAccount bindSubAccount = new BindSubAccount() ;
            bindSubAccount.setCreatedAt(date);
            bindSubAccount.setUpdatedAt(date);
            if (userByTel == null) {
                user.setOpenId(openId.getOpen_id());
                if (!StringUtils.isEmpty(guanaitongUserBean.getName())) {
                    user.setNickname(guanaitongUserBean.getName());
                } else {
                    String nickname = "fc_" + guanaitongUserBean.getOpen_id().substring(user.getOpenId().length() - 8);
                    user.setNickname(nickname);
                }
                user.setName(guanaitongUserBean.getName());
                user.setTelephone(guanaitongUserBean.getMobile());
                user.setiAppId(iAppId);
                user.setCreatedAt(new Date());
                userMapper.insertSelective(user);
                bindSubAccount.setUserId(user.getId());
                bindSubAccount.setOpenId(user.getOpenId());
                bindSubAccount.setAppId("00");
                // 添加子账户
                bindSubAccountMapper.insertSelective(bindSubAccount) ;
            } else {
                // 查询openId是否存在
                BindSubAccount checkBind = bindSubAccountDao.selectByUserIdAndAppId("00", userByTel.getId()) ;
                if (checkBind == null) {
                    bindSubAccount.setUserId(userByTel.getId());
                    bindSubAccount.setOpenId(openId.getOpen_id());
                    bindSubAccount.setAppId("00");
                    // 添加子账户
                    bindSubAccountMapper.insertSelective(bindSubAccount) ;
                    BeanUtils.copyProperties(userByTel, user);
                    accessToken.setOpenId(userByTel.getOpenId());
                }
            }
        }
        result.getData().put("result", accessToken);
        return result ;
    }

    @Override
    public OperaResult findThirdPartyTokenWX(String iAppId, String code) {
        OperaResult result = new OperaResult();
        AccessToken accessToken = new AccessToken() ;
        // 获取微信登录信息
        OperaResponse<WeChatAccessTokenBean> accessTokenBeanOperaResponse = weChatService.getAccessToken(iAppId, code) ;
        if (accessTokenBeanOperaResponse.getCode() != 200) {
            result.setCode(accessTokenBeanOperaResponse.getCode());
            result.setMsg(accessTokenBeanOperaResponse.getMsg());
            return result ;
        }
        WeChatAccessTokenBean weChatAccessTokenBean = accessTokenBeanOperaResponse.getData() ;
        accessToken.setOpenId(weChatAccessTokenBean.getOpenid());
        User temp = new User();
        temp.setOpenId(weChatAccessTokenBean.getOpenid());
        temp.setiAppId(iAppId);
        User user = userMapper.selectByOpenId(temp);
        if (user == null) {
            user = new User();
            if ("snsapi_base".equals(weChatAccessTokenBean.getScope())) {
                String nickname = "fc_" + weChatAccessTokenBean.getOpenid().substring(weChatAccessTokenBean.getOpenid().length() - 8);
                user.setNickname(nickname);
                user.setOpenId(weChatAccessTokenBean.getOpenid());
            } else {
                OperaResponse<WeChatUserInfoBean> userInfoBeanOperaResponse = weChatService.getUserInfo(weChatAccessTokenBean.getAccess_token(),weChatAccessTokenBean.getOpenid()) ;
                if (userInfoBeanOperaResponse.getCode() != 200) {
                    result.setCode(userInfoBeanOperaResponse.getCode());
                    result.setMsg(userInfoBeanOperaResponse.getMsg());
                    return result ;
                }
                WeChatUserInfoBean weChatUserInfoBean = userInfoBeanOperaResponse.getData() ;
                user.setOpenId(weChatUserInfoBean.getOpenid());
                if (!StringUtils.isEmpty(weChatUserInfoBean.getNickname())) {
                    user.setNickname(weChatUserInfoBean.getNickname());
                } else {
                    String nickname = "fc_" + weChatUserInfoBean.getOpenid().substring(user.getOpenId().length() - 8);
                    user.setNickname(nickname);
                }
                user.setName(weChatUserInfoBean.getNickname());
                if ("1".equals(weChatUserInfoBean.getSex())) {
                    user.setSex("男");
                } else if ("2".equals(weChatUserInfoBean.getSex())){
                    user.setSex("女");
                }
                user.setHeadImg(weChatUserInfoBean.getHeadimgurl());
            }
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
        AuthUserBean authUserBean = new AuthUserBean() ;
        // 获取平安用户信息
        OperaResponse<AuthUserBean> authUserBeanOperaResponse = pinganClientService.checkRequestCode(requestCode, iAppId) ;
        if (authUserBeanOperaResponse.getCode() != 200) {
            result.setCode(authUserBeanOperaResponse.getCode());
            result.setMsg(authUserBeanOperaResponse.getMsg());
            return result;
        }
        authUserBean = authUserBeanOperaResponse.getData();
        if (authUserBean ==null || authUserBean.getOpenId() == null || "".equals(authUserBean.getOpenId())) {
            result.setCode(9000001);
            result.setMsg("获取openId失败。");
            return  result;
        }
        String mobile = "";
        String authBeanNickname = "";
        if ("15".equals(iAppId)) {
            mobile = authUserBean.getMobile() ;
            authBeanNickname = authUserBean.getNickname() ;
        } else {
            mobile = authUserBean.getMobileNo() ;
            authBeanNickname = authUserBean.getNickName() ;
        }
        accessToken.setOpenId(authUserBean.getOpenId());
        accessToken.setPayId(authUserBean.getPayId());
        User temp = new User();
        temp.setOpenId(authUserBean.getOpenId());
        temp.setiAppId(iAppId);
        User user = userMapper.selectByOpenId(temp);
        SUser userByTel = userDao.selectUserByTel(iAppId, mobile) ;
        if (user == null) {
            // 生成自己的OpenId, 并且把第三方OpenId写到子账户
            Date date = new Date() ;
            user = new User();
            BindSubAccount bindSubAccount = new BindSubAccount() ;
            bindSubAccount.setCreatedAt(date);
            bindSubAccount.setUpdatedAt(date);
            if (userByTel == null) {
                user.setOpenId(authUserBean.getOpenId());
                if (!StringUtils.isEmpty(authBeanNickname)) {
                    user.setNickname(authBeanNickname);
                } else {
                    String nickname = "fc_" + authUserBean.getOpenId().substring(user.getOpenId().length() - 8);
                    user.setNickname(nickname);
                }
                user.setTelephone(mobile);
                user.setiAppId(iAppId);
                user.setCreatedAt(new Date());
                userMapper.insertSelective(user);

                bindSubAccount.setUserId(user.getId());
                bindSubAccount.setOpenId(user.getOpenId());
                bindSubAccount.setAppId("00");
                // 添加子账户
                bindSubAccountMapper.insertSelective(bindSubAccount) ;
            } else {
                // 查询openId是否存在
                BindSubAccount checkBind = bindSubAccountDao.selectByUserIdAndAppId("00", userByTel.getId()) ;
                if (checkBind == null) {
                    bindSubAccount.setUserId(userByTel.getId());
                    bindSubAccount.setOpenId(authUserBean.getOpenId());
                    bindSubAccount.setAppId("00");
                    // 添加子账户
                    bindSubAccountMapper.insertSelective(bindSubAccount) ;
                    BeanUtils.copyProperties(userByTel, user);
                    accessToken.setOpenId(userByTel.getOpenId());
                }
            }

        }
        if ("11".equals(iAppId) || "15".equals(iAppId)) {
            balanceDao.updateOpenIdByTel(user.getTelephone(), user.getOpenId());
        }
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
        response = weChatService.getAccessToken(appId, code) ;
        return response;
    }

    @Override
    public OperaResponse verifyCode(String telephone, String type, String appId, String appSrc) {
        OperaResponse result = new OperaResponse() ;
        if(StringUtil.isEmpty(telephone)){
            result.setCode(100000);
            result.setMsg("电话号码不正确");
            return result;
        }
        if(StringUtil.isEmpty(appSrc)){
            result.setCode(100000);
            result.setMsg("appSrc不正确");
            return result;
        }
        SMSPostBean smsPostBean = new SMSPostBean() ;
        smsPostBean.setPhone(telephone);
        String code = baseService.send(smsPostBean).getData() ;
        log.info("获取到的验证码是：", code);
        redisDAO.setKey(type + ":sso:" + appId + appSrc + telephone, code, 5 * 60 * 1000);
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
        if(StringUtil.isEmpty(bandWXBean.getAppSrc())){
            result.setCode(100000);
            result.setMsg("appSrc不正确");
            return result;
        }
        String code = redisDAO.getValue("wx:sso:" + bandWXBean.getAppId() + bandWXBean.getAppSrc() + bandWXBean.getTelephone()) ;
        if (bandWXBean.getCode().equals(code)) {
            // 绑定openId 查询手机号是否存在，绑定公众号
            SUser user = userDao.selectUserByTel(bandWXBean.getAppId(), bandWXBean.getTelephone()) ;
            //  如果用户不存在，创建用户ID，继续绑定用户, 输入手机号、appId 创建用户
            if (user == null) {
                user = new SUser() ;
                user.setiAppId(bandWXBean.getAppId());
                user.setTelephone(bandWXBean.getTelephone());
                // 生成OpenId和昵称
                user.setOpenId(Md5Util.md5(user.getTelephone() + user.getiAppId()));
                String nickname = "fc_" + user.getOpenId().substring(user.getOpenId().length() - 8);
                user.setNickname(nickname);
                SUser verify = userDao.selectUserByAppIdAndOpenId(user.getiAppId(), user.getOpenId()) ;
                if (verify == null) {
                    mapper.insertSelective(user) ;
                }
            }
            // 绑定第三方openId
            BindSubAccount bindSubAccount = bindSubAccountDao.selectByUserIdAndAppId(bandWXBean.getAppSrc(), user.getId()) ;
            Date date = new Date() ;
            if (bindSubAccount == null) {
                bindSubAccount = new BindSubAccount() ;
                bindSubAccount.setCreatedAt(date);
                bindSubAccount.setUpdatedAt(date);
                bindSubAccount.setUserId(user.getId());
                bindSubAccount.setOpenId(bandWXBean.getOpenId());
                bindSubAccount.setAppId(bandWXBean.getAppSrc());
                // 添加
                bindSubAccountMapper.insertSelective(bindSubAccount) ;
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
    public OperaResponse wxBindVerify(String appId, String appSrc, String openId) {
        log.info(appId, appSrc, openId);
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
        if(StringUtil.isEmpty(appSrc)){
            result.setCode(100000);
            result.setMsg("appSrc不正确");
            return result;
        }
        List<BindSubAccount> bindSubAccounts = bindSubAccountDao.selectByOpenIdAndAppId(appSrc, openId) ;
        if (!bindSubAccounts.isEmpty()) {
            for (BindSubAccount account : bindSubAccounts) {
                SUser user = mapper.selectByPrimaryKey(account.getUserId()) ;
                if (appId.equals(user.getiAppId())) {
                    result.setData(user);
                }
            }
        }
        return result;
    }

    @Override
    public TokenBean login(LoginBean loginBean) {
        TokenBean tokenBean = new TokenBean();
        String token = JwtTokenUtil.generateToken(loginBean);
        SUser user = userDao.selectUserByTel(loginBean.getAppId(), loginBean.getUsername()) ;
        tokenBean.setToken(token);
        tokenBean.setOpenId(user.getOpenId());
        redisDAO.setKey("sso:" + loginBean.getAppId() + loginBean.getUsername(), token, JwtTokenUtil.EXPIRATIONTIME);
        return tokenBean;
    }

    @Override
    public SUser findByUser(LoginBean loginBean) {
        return userDao.selectUserByTel(loginBean.getAppId(), loginBean.getUsername());
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

    private OpenId getGuanaitongOpenId(String authCode, String appId) {
        AuthCode authCode1 = new AuthCode();
        authCode1.setAuth_code(authCode);
        Result result = guanaitongClientService.findOpenId(authCode1, appId);
        if (result.getCode() == 200) {
            Object object = result.getData() ;
            String jsonString = JSON.toJSONString(object);
            OpenId openId = JSONObject.parseObject(jsonString, OpenId.class) ;
            return openId;
        }
        return null;
    }

    private GuanaitongUserBean getGuanaitongUser(String openId, String appId) {
        OpenId openId1 = new OpenId();
        openId1.setOpen_id(openId);
        Result result = guanaitongClientService.findUser(openId1, appId);
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
