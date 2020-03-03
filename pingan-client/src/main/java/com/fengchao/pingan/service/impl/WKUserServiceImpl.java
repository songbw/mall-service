package com.fengchao.pingan.service.impl;

import com.fengchao.pingan.bean.OperaResponse;
import com.fengchao.pingan.bean.WKOperaResponse;
import com.fengchao.pingan.bean.WKUserInfo;
import com.fengchao.pingan.bean.WKUserRequestBean;
import com.fengchao.pingan.config.PingAnClientConfig;
import com.fengchao.pingan.service.WKUserService;
import com.fengchao.pingan.utils.HttpClient;
import com.fengchao.pingan.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author song
 * @2020-02-26 4:24 下午
 **/
@Slf4j
@EnableConfigurationProperties({PingAnClientConfig.class})
@Service
public class WKUserServiceImpl implements WKUserService {

    @Autowired
    private PingAnClientConfig config;

    @Override
    public WKOperaResponse<WKUserInfo> getWKUserInfo(WKUserRequestBean bean) {
        WebTarget webTarget = HttpClient.createClient().target(config.getWkBaseUrl() + HttpClient.WK_USER_INFO_URL);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(bean, MediaType.APPLICATION_JSON));
        WKOperaResponse result = response.readEntity(WKOperaResponse.class);
        log.info("获取万科用户信息返回值： {}", JSONUtil.toJsonString(result));
        return result ;
    }
}
