package com.fengchao.gateway.filter;

import com.fengchao.gateway.config.GatewayConfig;
import com.fengchao.gateway.utils.JwtHelper;
import com.fengchao.gateway.utils.RedisDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@EnableConfigurationProperties({GatewayConfig.class})
@Slf4j
@Component
public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {

    private static final String AUTHORIZE_TOKEN = "Authorization";
    private static final String AUTHORIZE_UID = "uid";

    public AuthorizeGatewayFilterFactory() {
        super(Config.class);
        log.info("Loaded AuthorizeGatewayFilterFactory [Authorize]");
    }

    @Autowired
    private RedisDAO redisDAO;
    @Autowired
    private GatewayConfig gatewayConfig;

    @Override
    public GatewayFilter apply(AuthorizeGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String url = request.getURI().getPath();
            if (url.indexOf("/hello") >= 0) {
                response.setStatusCode(HttpStatus.OK);
                return response.setComplete();
            }
            //忽略以下url请求
            for (String unAuth: gatewayConfig.getNoAuthorization()) {
                if(url.indexOf(unAuth) >= 0){
                    return chain.filter(exchange);
                }
            }
            HttpHeaders headers = request.getHeaders();
            /**
             * Authorization 格式 type + " " + tokenValue
             * 管理端token格式："Bearer" + " " + tokenValue
             * 客户端端token格式："token" + " " + tokenValue
             */
            String authorization = headers.getFirst(AUTHORIZE_TOKEN);

            if (StringUtils.isEmpty(authorization)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            String auth[] = authorization.split(" ");
            if (auth.length < 2) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            // type
            String type = auth[0];
            // token value
            String tokenValue = auth[1];
            String jwtValue = "";
            try {
                jwtValue = JwtHelper.getValue(tokenValue) ;
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            if (jwtValue == null || "".equals(jwtValue)){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            String redisKey = "" ;
            if ("token".equals(type)) {
                redisKey = "sso:" + jwtValue;
            } else if ("Bearer".equals(type)) {
                redisKey = "vendor:" + jwtValue;
            }
            String redisValue = redisDAO.getValue(redisKey);
            if (redisValue == null || "".equals(redisValue)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            if (!tokenValue.equals(redisValue)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            if ("Bearer".equals(type)) {
                headers.add("username", jwtValue);
            }
            return chain.filter(exchange);
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("enabled");
    }

    public static class Config {
        // 控制是否开启认证
        private boolean enabled;

        public Config() {

        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
