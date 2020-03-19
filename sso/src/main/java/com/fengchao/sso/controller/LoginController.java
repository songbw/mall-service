package com.fengchao.sso.controller;

import com.fengchao.sso.bean.*;
import com.fengchao.sso.config.SMSConfig;
import com.fengchao.sso.feign.BaseService;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.service.ILoginService;
import com.fengchao.sso.util.*;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/sso", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {

    @Autowired
    private ILoginService loginService;
    @Autowired
    private RedisDAO redisDAO;
    @Autowired
    private SMSUtil smsUtil;
    @Autowired
    private BaseService baseService ;
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/register")
    private OperaResult register(@RequestBody LoginBean loginBean){
        OperaResult result = new OperaResult();
        Login login = loginService.selectByPrimaryName(loginBean.getUsername());
        if(login != null){
            result.setCode(10007);
            result.setMsg("用户名已存在");
            return result;
        }
        String value = redisDAO.getValue("zc:sso:" + loginBean.getAppId() + loginBean.getUsername()) ;
        if(StringUtils.isEmpty(value)) {
            result.setCode(10008);
            result.setMsg("验证码不正确");
            return result;
        }
//            String value = redisDAO.getValue(loginBean.getUsername());
        if(!value.equals(loginBean.getCode())){
            result.setCode(10008);
            result.setMsg("验证码不正确");
            return result;
        }
        if(StringUtil.isNotEmpty(loginBean.getUsername()) && StringUtil.isNotEmpty(loginBean.getPassword())){
            loginService.insertSelective(loginBean) ;
        }
        result.getData().put("result",loginService.login(loginBean));
        return result;
    }

    @GetMapping("/getVerificationCode")
    public OperaResult getVerificationCode(String username, OperaResult result) {
        if(StringUtil.isEmpty(username)){
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        String code = smsUtil.getFourRandom();
        String[] params = {code,SMSConfig.TENT_ActiveTime};
        String string = smsUtil.sendMesModel(params, username,SMSConfig.TENT_TemplateID1);

        result.setMsg(string);
        redisDAO.setKey(username, code, JwtTokenUtil.EXPIRATIONTIME);
        return result;
    }

    @PostMapping("/login")
    public OperaResult getPasswordLogin(@RequestBody LoginBean loginBean, OperaResult result) {
        Login login = null;
        if(StringUtil.isNotEmpty(loginBean.getUsername()) && StringUtil.isNotEmpty(loginBean.getPassword())){
            login = loginService.selectByPrimaryName(loginBean.getUsername());
        }
        if (login == null) {
            // 用户名不正确
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        if(!loginBean.getPassword().equals(login.getPassword())){
            // 密码不正确
            result.setCode(100001);
            result.setMsg("密码不正确");
            return result;
        }
       result.getData().put("result", loginService.login(loginBean));
        return result;
    }

    @PostMapping("/thirdLogin")
    public OperaResult thirdLogin(@RequestBody ThirdLoginBean loginBean, OperaResult result) {
        if (StringUtils.isEmpty(loginBean.getOpenId())) {
            result.setCode(100000);
            result.setMsg("openId不能为空");
            return result;
        }
        result.getData().put("result", loginService.thirdLogin(loginBean));
        return result;
    }

    @PostMapping("/login/code")
    public OperaResult getCodeLogin(@RequestBody LoginBean loginBean, OperaResult result) {
        Login login = null;
        if(StringUtil.isNotEmpty(loginBean.getUsername()) && StringUtil.isNotEmpty(loginBean.getCode())){
            login = loginService.selectByPrimaryName(loginBean.getUsername());
        }
        if (login == null) {
            // 用户名不正确
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        String value = redisDAO.getValue(loginBean.getUsername());
        if(value == null || !value.equals(loginBean.getCode())){
            result.setCode(10008);
            result.setMsg("验证码不正确");
            return result;
        }
        String token = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        result.getData().put("Token", token);
        redisDAO.setKey(loginBean.getUsername(), token, JwtTokenUtil.EXPIRATIONTIME);
        return result;
    }

    @DeleteMapping("/logout")
    public OperaResult logout(String username, OperaResult result) {
        if (StringUtil.isEmpty(username)) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        redisDAO.removeValue(username) ;
        return result;
    }

    @PutMapping("/password/update")
    public OperaResult updatePassword(@RequestBody LoginBean loginBean, OperaResult result) {
        if (StringUtil.isEmpty(loginBean.getUsername())) {
            result.setCode(100000);
            result.setMsg("用户名不为空");
            return result;
        }
        if (StringUtil.isEmpty(loginBean.getPassword())) {
            result.setCode(100001);
            result.setMsg("密码不为空");
            return result;
        }
        if (StringUtil.isEmpty(loginBean.getOldPassword())) {
            result.setCode(100005);
            result.setMsg("旧密码不为空");
            return result;
        }
        Login login = loginService.selectByPrimaryName(loginBean.getUsername());
        if (login == null) {
            result.setCode(100000);
            result.setMsg("用户名不正确");
            return result;
        }
        if (!loginBean.getOldPassword().equals(login.getPassword())) {
            result.setCode(100006);
            result.setMsg("当前密码不正确");
            return result;
        }
        loginService.updatePassword(loginBean);
        return  result;
    }

    @PutMapping("/password/forget")
    public OperaResult forgetPassword(@RequestBody LoginBean loginBean, OperaResult result) {
        if (StringUtil.isEmpty(loginBean.getUsername())) {
            result.setCode(100000);
            result.setMsg("用户名不为空");
            return result;
        }
        if (StringUtil.isEmpty(loginBean.getPassword())) {
            result.setCode(100001);
            result.setMsg("密码不为空");
            return result;
        }
        if (StringUtil.isEmpty(loginBean.getCode())) {
            result.setCode(100003);
            result.setMsg("验证码不为空");
            return result;
        }
        String value = redisDAO.getValue("fp:sso:" + loginBean.getAppId() + loginBean.getUsername()) ;
        if(StringUtils.isEmpty(value)) {
            result.setCode(10008);
            result.setMsg("验证码不正确");
            return result;
        }
//        String value = redisDAO.getValue(loginBean.getUsername());
        if(!value.equals(loginBean.getCode())){
            result.setCode(100004);
            result.setMsg("验证码不正确");
            return result;
        }
        loginService.updatePassword(loginBean) ;
        return  result;
    }

    @GetMapping("/thirdParty/token")
    public OperaResult getThirdOpenId(String iAppId, String requestCode) {
        OperaResult result = new OperaResult();
        if (StringUtil.isEmpty(requestCode)){
            result.setCode(100000);
            result.setMsg("requestCode不正确");
            return result;
        }
        return loginService.getPingAnOpenId(iAppId, requestCode) ;
    }

    @GetMapping("/thirdParty/token/gat")
    public OperaResult getThirdOpenIdGAT(String iAppId, String initCode) {
        OperaResult result = new OperaResult();
        if (StringUtil.isEmpty(initCode)){
            result.setCode(100000);
            result.setMsg("initCode不正确");
            return result;
        }
        return loginService.findThirdPartyTokenGAT(iAppId, initCode) ;
    }

    @GetMapping("wx")
    public OperaResponse getWXOpenIdByAppIdAndCode(String appId, String code) {
        return loginService.getWXOpenIdByAppIdAndCode(appId, code) ;
    }

    @GetMapping("/code")
    public OperaResponse verifyCode(@RequestHeader("appId") String appId, String telephone, String type) {
        return loginService.verifyCode(telephone, type, appId);
    }

    @PutMapping("/wx/bind")
    public OperaResponse wxBind(@RequestHeader("appId") String appId, @RequestBody BindWXBean bindWXBean) {
        return loginService.bindWXOpenId(bindWXBean);
    }

    @GetMapping("/wx/bind/verify")
    public OperaResponse wxBindVerify(String appId, String openSrc) {
        return loginService.wxBindVerify(appId, openSrc);
    }

    @GetMapping("/thirdParty/token/wx")
    public OperaResult getThirdOpenIdWX(String iAppId, String code) {
        OperaResult result = new OperaResult() ;
        if (StringUtil.isEmpty(code)){
            result.setCode(100000);
            result.setMsg("code不正确");
            return result;
        }
        return loginService.findThirdPartyTokenWX(iAppId, code) ;
    }
}

