package com.fengchao.sso.service.impl;

import com.fengchao.sso.bean.LoginBean;
import com.fengchao.sso.bean.ThirdLoginBean;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
            User user = userMapper.selectByOpenId(loginBean.getOpenId());
            if (user == null) {
                user = new User();
                user.setOpenId(loginBean.getOpenId());
                String nickname = "fc_" + user.getOpenId().substring(user.getOpenId().length() - 8);
                user.setNickname(nickname);
                user.setCreatedAt(new Date());
                userMapper.insertSelective(user);
            }
        }
        RedisUtil.putRedis("User:" + token, loginBean.getOpenId(), RedisUtil.appexpire);
        return tokenBean;
    }

    @Override
    public OperaResult findPingAnToken(String initCode) {
        return pinganClientService.findToken(initCode);
    }

}
