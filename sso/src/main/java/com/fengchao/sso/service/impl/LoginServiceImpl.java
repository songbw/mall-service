package com.fengchao.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.sso.bean.*;
import com.fengchao.sso.feign.GuanaitongClientService;
import com.fengchao.sso.feign.PinganClientService;
import com.fengchao.sso.mapper.LoginMapper;
import com.fengchao.sso.mapper.custom.LoginCustomMapper;
import com.fengchao.sso.mapper.TokenMapper;
import com.fengchao.sso.mapper.UserMapper;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.Token;
import com.fengchao.sso.model.User;
import com.fengchao.sso.service.ILoginService;
import com.fengchao.sso.util.OperaResult;
import com.fengchao.sso.util.RedisUtil;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

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
    public Token thirdLogin(ThirdLoginBean loginBean) {
        Token tokenBean = new Token();
        String token = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        tokenBean.setToken(token);
        tokenBean.setOpenId(loginBean.getOpenId());
        tokenBean.setThirdToken(loginBean.getAccessToken());
        tokenBean.setExpireDate(RedisUtil.appexpire);
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
                user = new User();
                user.setOpenId(loginBean.getOpenId());
                String nickname = "fc_" + user.getOpenId().substring(user.getOpenId().length() - 8);
                user.setNickname(nickname);
                user.setCreatedAt(new Date());
                user.setiAppId(loginBean.getiAppId());
                userMapper.insertSelective(user);
            }
        }
        RedisUtil.putRedis("User:" + token, loginBean.getOpenId(), RedisUtil.appexpire);
        return tokenBean;
    }

    @Override
    public OperaResult findThirdPartyToken(String iAppId, String initCode) {
        OperaResult result = new OperaResult();
        AccessToken accessToken = new AccessToken() ;
        if ("10".equals(iAppId)) {
            // 获取关爱通登录信息
            OpenId openId = getGuanaitongOpenId(initCode) ;
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
        } else {
            accessToken = getPingAnToken(initCode) ;
        }
        result.getData().put("result", accessToken);
        return result ;
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

}
