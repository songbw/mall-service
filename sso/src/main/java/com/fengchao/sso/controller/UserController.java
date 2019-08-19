package com.fengchao.sso.controller;

import com.fengchao.sso.bean.LoginBean;
import com.fengchao.sso.bean.UserBean;
import com.fengchao.sso.util.*;
import com.github.pagehelper.util.StringUtil;
import com.fengchao.sso.config.SMSConfig;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.User;
import com.fengchao.sso.service.ILoginService;
import com.fengchao.sso.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class UserController {

    @Autowired
    private IUserService service;

    @Autowired
    private ILoginService loginService;
    @Autowired
    private RedisDAO redisDAO;

    @PutMapping
    private OperaResult update(@RequestBody UserBean bean, OperaResult result){
        int id = service.update(bean);
        result.getData().put("result", id);
        return result;
    }

    @GetMapping
    public OperaResult findByOpenId(String openId, String iAppId, OperaResult result) {
        if(StringUtil.isEmpty(openId)){
            result.setCode(100000);
            result.setMsg("openId不能为空");
            return result;
        }
        User user = service.selectUserByOpenId(openId, iAppId);
        result.getData().put("user",user);
        return result;
    }

    @GetMapping("/list")
    public OperaResult getProfileList(Integer pageNo, Integer pageSize, OperaResult result){
        List<User> userList = service.selectUser(pageNo, pageSize);
        result.getData().put("userList",userList);
        return result;
    }

    @PostMapping("/activeProfile")
    public OperaResult getActiveProfile(@RequestBody List<String> tokens, OperaResult result){
        List<UserBean> userList = new ArrayList<UserBean>();
        for (String token:tokens){
            String username = "" ;
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
        redisDAO.removeValue(user.getUsername()) ;
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
        log.info("查询用户总数,入参:无");
        result.getData().put("count", service.findUserCount());
        log.info("查询用户总数,返回:{}", JSONUtil.toJsonString(result));
        return result;
    }
}

