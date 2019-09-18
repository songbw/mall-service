package com.fengchao.pingan.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.exception.PinganClientException;
import com.fengchao.pingan.service.UserService;
import com.fengchao.pingan.utils.DateUtils;
import com.fengchao.pingan.utils.HttpClient;
import com.fengchao.pingan.utils.Pkcs8Util;
import com.fengchao.pingan.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public TokenResult getAccessToken(String initCode) throws PinganClientException {
        WebTarget webTarget = HttpClient.createClient().target(HttpClient.ACCESS_TOKEN);
        ZhcsResquest resquest = new ZhcsResquest();
        TokenParam tokenParam = new TokenParam();
        tokenParam.setRandomSeries(RandomUtil.getRandomString(10));
        tokenParam.setTimestamp(DateUtils.dateFormat());
        tokenParam.setInitCode(initCode);
        resquest.setMessage(tokenParam);
        ObjectMapper objectMapper = new ObjectMapper();
        String messageString = null;
        try {
            messageString = objectMapper.writeValueAsString(resquest.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            String sign = Pkcs8Util.getSign(messageString);
            resquest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String requestString = null;
        try {
            requestString = objectMapper.writeValueAsString(resquest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(requestString, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
            return  response.readEntity(TokenResult.class);
        } else {
            return null;
        }
    }

    @Override
    public UserResult getUserInfo(String userToken) throws PinganClientException {
        WebTarget webTarget = HttpClient.createClient().target(HttpClient.USER_INFO);
        ZhcsResquest resquest = new ZhcsResquest();
        UserParam userParam = new UserParam();
        userParam.setRandomSeries(RandomUtil.getRandomString(10));
        userParam.setTimestamp(DateUtils.dateFormat());
        userParam.setUserToken(userToken);
        resquest.setMessage(userParam);
        ObjectMapper objectMapper = new ObjectMapper();
        String messageString = null;
        try {
            messageString = objectMapper.writeValueAsString(resquest.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            String sign = Pkcs8Util.getSign(messageString);
            resquest.setSign(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String requestString = null;
        try {
            requestString = objectMapper.writeValueAsString(resquest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(requestString, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
            return  response.readEntity(UserResult.class);
        } else {
            return null;
        }
    }

    @Override
    public OperaResponse<InitCodeBean> getInitCode(InitCodeRequestBean bean) {
        WebTarget webTarget = HttpClient.createClient().target(HttpClient.USER_INFO);
        bean.setAppId("ea70244ca3604a4ebc1c2fd8e48756d5");
        bean.setTimestamp(new Date().getTime() + "");
        bean.setRandomSeries(RandomUtil.getRandomString(10));
        ObjectMapper objectMapper = new ObjectMapper();
        String messageString = null;
        try {
            messageString = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String requestString = null;
        try {
            requestString = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(requestString, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
//            return  response.readEntity(UserResult.class);
            return null;
        } else {
            return null;
        }
    }
}
