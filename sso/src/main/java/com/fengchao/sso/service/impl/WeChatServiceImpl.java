package com.fengchao.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.sso.bean.*;
import com.fengchao.sso.config.SSOConfiguration;
import com.fengchao.sso.service.WeChatService;
import com.fengchao.sso.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author songbw
 * @date 2019/12/13 10:52
 */
@Slf4j
@EnableConfigurationProperties({SSOConfiguration.class})
@Service
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private SSOConfiguration ssoConfiguration;

    @Override
    public OperaResponse<WeChatAccessTokenBean> getAccessToken(String appId, String code) {
        OperaResponse response = new OperaResponse() ;
        SSOConfigBean configBean = getSSOConfig(appId) ;
        String bean = HttpClient.getAccessToken(configBean.getWxAppId(), configBean.getWxAppSecret(), code, String.class) ;
        log.info("微信获取accessToken返回值： " + bean);
        JSONObject jsonObject = JSON.parseObject(bean) ;
        if (bean == null) {
            response.setCode(900003);
            response.setMsg("获取微信openId失败");
            return response ;
        }
        String openId = jsonObject.getString("openid") ;
        if (StringUtils.isEmpty(openId)) {
            WeChatErrorBean weChatErrorBean = JSONObject.parseObject(bean, WeChatErrorBean.class) ;
            response.setCode(weChatErrorBean.getErrcode());
            response.setMsg(weChatErrorBean.getErrmsg());
            return response ;
        }
        WeChatAccessTokenBean weChatAccessTokenBean = JSONObject.parseObject(bean, WeChatAccessTokenBean.class) ;
        response.setData(weChatAccessTokenBean);
        return response;
    }

    @Override
    public OperaResponse<WeChatUserInfoBean> getUserInfo(String accessToken, String openId) {
        OperaResponse response = new OperaResponse() ;
        String bean = HttpClient.getUserInfo(accessToken, openId, String.class) ;
        log.info("微信获取用户个人信息返回值： " + bean);
        JSONObject jsonObject = JSON.parseObject(bean) ;
        if (bean == null) {
            response.setCode(900003);
            response.setMsg("获取微信用户个人信息失败");
            return response ;
        }
        String nickname = jsonObject.getString("nickname") ;
        if (StringUtils.isEmpty(nickname)) {
            WeChatErrorBean weChatErrorBean = JSONObject.parseObject(bean, WeChatErrorBean.class) ;
            response.setCode(weChatErrorBean.getErrcode());
            response.setMsg(weChatErrorBean.getErrmsg());
            return response ;
        }
        WeChatUserInfoBean weChatUserInfoBean = JSONObject.parseObject(bean, WeChatUserInfoBean.class) ;
        response.setData(weChatUserInfoBean);
        return response;
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
