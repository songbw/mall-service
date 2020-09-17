package com.fengchao.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@EnableConfigurationProperties({GatewayConfig.class})
@Configuration
public class RouteConfiguration {

    @Autowired
    private GatewayConfig gatewayConfig ;

    //这里为支持的请求头，如果有自定义的header字段请自己添加（不知道为什么不能使用*）
    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,token,username,client, merchant, appId, renterId, merchantId, renter";
    private static final String ALLOWED_METHODS = "POST, PUT, GET, DELETE, OPTIONS, PATCH";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_Expose = "*";
    private static final String MAX_AGE = "18000L";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                List<String> allowDomain = gatewayConfig.getOrigins();
                String origin = request.getHeaders().getOrigin();
                if (allowDomain.contains(origin)) {
                    log.info("Access-Control-Allow-Origin " + origin);
                    headers.add("Access-Control-Allow-Origin", origin);
                    headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                    headers.add("Access-Control-Max-Age", MAX_AGE);
                    headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
//                    headers.add("Access-Control-Expose-Headers", ALLOWED_Expose);
                    headers.add("Access-Control-Allow-Credentials", "true");
                    headers.add("X-Content-Type-Options", "nosniff");
                    headers.add("X-XSS-Protection", "1; mode=block");
                    headers.add("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
                    headers.add("Pragma", "no-cache");
                    headers.add("Expires", "0");
                    headers.add("X-Frame-Options", "DENY");
                }
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

}
