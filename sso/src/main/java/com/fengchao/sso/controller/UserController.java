package com.fengchao.sso.controller;

import com.fengchao.sso.bean.LoginBean;
import com.fengchao.sso.bean.UserBean;
import com.github.pagehelper.util.StringUtil;
import com.fengchao.sso.config.SMSConfig;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.User;
import com.fengchao.sso.service.ILoginService;
import com.fengchao.sso.service.IUserService;
import com.fengchao.sso.util.Md5Util;
import com.fengchao.sso.util.OperaResult;
import com.fengchao.sso.util.RedisUtil;
import com.fengchao.sso.util.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired
    private IUserService service;

    @Autowired
    private ILoginService loginService;

    @PutMapping
    private OperaResult update(@RequestBody UserBean bean, OperaResult result){
        int id = service.update(bean);
        result.getData().put("result", id);
        return result;
    }

    @GetMapping
    public OperaResult findByOpenId(String openId, OperaResult result) {
        if(StringUtil.isEmpty(openId)){
            result.setCode(100000);
            result.setMsg("openId不能为空");
            return result;
        }
        User user = service.selectUserByOpenId(openId);
        result.getData().put("user",user);
        return result;
    }

    @GetMapping("/profileList")
    public OperaResult getProfileList(Integer page, Integer limit, OperaResult result){
        List<User> userList = service.selectUser(page, limit);
        result.getData().put("userList",userList);
        return result;
    }

    @PostMapping("/activeProfile")
    public OperaResult getActiveProfile(@RequestBody List<String> tokens, OperaResult result){
        List<UserBean> userList = new ArrayList<UserBean>();
        for (String token:tokens){
            String username = RedisUtil.getKey(token);
            if(username != null || !username.equals("")){
                Map<String, Object> map=new HashMap<>();
                map.put("username", username);
                UserBean userBean = service.selectUserByname(map);
                userList.add(userBean);
            }
        }
        result.getData().put("userList",userList);
        return result;
    }

    @DeleteMapping("/logout")
    public OperaResult logout(@RequestBody UserBean bean, OperaResult result) {
        Map<String, Object> map=new HashMap<>();
        Login user = service.selectuserById(bean);
        RedisUtil.removeValue(user.getUsername()) ;
        return result;
    }

    @PostMapping("/activeOrLock")
    private OperaResult activeOrLock(@RequestBody UserBean bean){
        OperaResult result = new OperaResult();
        service.updateByPrimaryKey(bean);
        return result;
    }

    @PostMapping("/resetPassword")
    private OperaResult resetPassword(@RequestBody UserBean bean, OperaResult result){
        LoginBean loginBean = new LoginBean();
        Login login = service.selectuserById(bean);

        String code = SMSUtil.getFourRandom();
        String[] params = {code};
        String string = SMSUtil.sendMesModel(params,login.getUsername(), SMSConfig.TENT_TemplateID2);

        loginBean.setUsername(login.getUsername());
        loginBean.setPassword(Md5Util.md5(code));
        loginService.updatePassword(loginBean);

        result.setMsg(string);
        return result;
    }

    @GetMapping("/pingan")
    public OperaResult findPingAnUser(String userToken, OperaResult result) {
        if(StringUtil.isEmpty(userToken)){
            result.setCode(100000);
            result.setMsg("userToken不能为空");
            return result;
        }
        return service.findPingAnUser(userToken);
    }

    @GetMapping("/count")
    public OperaResult count(OperaResult result) {
        result.getData().put("count", service.findUserCount());
        return result;
    }
}

