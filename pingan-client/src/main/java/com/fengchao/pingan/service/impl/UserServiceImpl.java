package com.fengchao.pingan.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.config.PingAnClientConfig;
import com.fengchao.pingan.exception.PinganClientException;
import com.fengchao.pingan.service.UserService;
import com.fengchao.pingan.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@EnableConfigurationProperties({PingAnClientConfig.class})
@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private PingAnClientConfig config;

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
    public OperaResponse<InitCodeBean> getInitCode() {
        WebTarget webTarget = HttpClient.createClient().target(config.getAuthBasePath() + HttpClient.INIT_CODE_URL);
        InitCodeRequestBean bean = new InitCodeRequestBean();
        bean.setAppId(config.getAppId());
        bean.setTimestamp(new Date().getTime() + "");
        bean.setRandomSeries(RandomUtil.getRandomNumber(10));
        bean.setCipherText(Pkcs8Util.getCiphe(bean, config.getAppKey()));
        logger.info("??????init code ????????? {}", JSONUtil.toJsonString(bean));
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        OperaResponse<InitCodeBean> result = response.readEntity(OperaResponse.class);
//        OperaResponse<InitCodeBean> result = null ;
//        String res = response.readEntity(String.class);
        logger.info("??????init code ???????????? {}", JSONUtil.toJsonString(result));
//        logger.info("??????init code ???????????? {}", res);
        return  result ;
    }

    @Override
    public OperaResponse<AuthCodeBean> getAuthCode() {
        WebTarget webTarget = HttpClient.createClient().target(config.getAuthBasePath()+ HttpClient.AUTH_CODE_URL);
        InitCodeRequestBean bean = new InitCodeRequestBean();
        bean.setAppId(config.getAppId());
        bean.setTimestamp(new Date().getTime() + "");
        bean.setRandomSeries(RandomUtil.getRandomNumber(10));
        bean.setCipherText(Pkcs8Util.getCiphe(bean, config.getAppKey()));
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        OperaResponse<AuthCodeBean> result = response.readEntity(OperaResponse.class);
        logger.info("??????auth code ???????????? {}", JSONUtil.toJsonString(result));
        return  result ;
    }

    @Override
    public OperaResponse<AccessToken> getAuthAccessToken(String authCode) {
        WebTarget webTarget = HttpClient.createClient().target(config.getAuthBasePath()+ HttpClient.ACCESS_TOKEN_URL);
        AccessTokenRequestBean bean = new AccessTokenRequestBean();
        bean.setAuthCode(authCode);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        OperaResponse<AccessToken> result = response.readEntity(OperaResponse.class);
        logger.info("??????auth access token ???????????? {}", JSONUtil.toJsonString(result));
        return  result ;
    }

    @Override
    public OperaResponse<AccessToken> getRefreshToken(String refreshToken) {
        WebTarget webTarget = HttpClient.createClient().target(config.getAuthBasePath()+ HttpClient.REFRESH_TOKEN_URL);
        RefreshTokenRequestBean bean = new RefreshTokenRequestBean();
        bean.setRefreshToken(refreshToken);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        OperaResponse<AccessToken> result = response.readEntity(OperaResponse.class);
        logger.info("refresh Token???????????? {}", JSONUtil.toJsonString(result));
        return  result ;
    }

    @Override
    public OperaResponse<CheckTokenBean> checkToken(String accessToken) {
        WebTarget webTarget = HttpClient.createClient().target(config.getAuthBasePath()+ HttpClient.CHECK_TOKEN_URL);
        CheckTokenRequestBean bean = new CheckTokenRequestBean();
        bean.setAccessToken(accessToken);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        return  response.readEntity(OperaResponse.class);
    }

    @Override
    public OperaResponse checkRequestCode(String requestCode) {
        logger.info("?????? RequestCode????????????{}", requestCode);
        OperaResponse<AuthCodeBean> authCodeBeanOperaResponse = getAuthCode();

        if (authCodeBeanOperaResponse.getCode() != 200) {
            return authCodeBeanOperaResponse;
        }
        AuthCodeBean authCodeBean = JSON.parseObject(JSON.toJSONString(authCodeBeanOperaResponse.getData()), AuthCodeBean.class);
        OperaResponse<AccessToken> accessTokenOperaResponse =  getAuthAccessToken(authCodeBean.getAuthCode()) ;
        if (accessTokenOperaResponse.getCode() != 200) {
            return accessTokenOperaResponse;
        }
        AccessToken accessToken = JSON.parseObject(JSON.toJSONString(accessTokenOperaResponse.getData()), AccessToken.class);
        CheckRequestCodeRequestBean bean = new CheckRequestCodeRequestBean();
        bean.setRequestCode(requestCode);
        bean.setAccessToken(accessToken.getAccessToken());
        WebTarget webTarget = HttpClient.createClient().target(config.getAuthBasePath()+ HttpClient.CHECK_REQUEST_CODE_URL);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        OperaResponse result = response.readEntity(OperaResponse.class);
        logger.info("?????? RequestCode???????????? {}", JSONUtil.toJsonString(result));
        return  result ;
    }

    @Override
    public OperaResponse<AuthUserBean> getAuthUserInfo(String userAccessToken) {
        logger.info("??????????????????????????????{}", userAccessToken);
        AuthUserRequestBean bean = new AuthUserRequestBean();
        bean.setUserAccessToken(userAccessToken);
        WebTarget webTarget = HttpClient.createClient().target(config.getAuthBasePath()+ HttpClient.USER_iNFO_URL);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        OperaResponse result = response.readEntity(OperaResponse.class);
        logger.info("?????????????????????????????? {}", JSONUtil.toJsonString(result));
        return  result ;
    }

    @Override
    public OperaResponse<AuthUserBean> getAuthUserInfoByRequestCode(String requestCode) {
        OperaResponse requestCodeResponse =  checkRequestCode(requestCode) ;
        if (requestCodeResponse.getCode() != 200) {
            return requestCodeResponse;
        }
        RequestCodeBean requestCodeBean = JSON.parseObject(JSON.toJSONString(requestCodeResponse.getData()), RequestCodeBean.class);
        OperaResponse<AuthUserBean> userBeanOperaResponse =  getAuthUserInfo(requestCodeBean.getUserAccessToken()) ;
        return userBeanOperaResponse;
    }
}
