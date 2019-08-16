package com.fengchao.gateway.filter;

import com.fengchao.gateway.utils.JwtHelper;
import com.fengchao.gateway.utils.RedisDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public GatewayFilter apply(AuthorizeGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }
            ServerHttpRequest request = exchange.getRequest();
            String url = request.getURI().getPath();
            //忽略以下url请求
            if(url.indexOf("/toushi/") >= 0 || url.indexOf("login") >= 0 || url.indexOf("/thirdParty/token") >= 0 || url.indexOf("/thirdLogin") >= 0 || url.indexOf("/v2/vendors/vendors") >= 0 || url.indexOf("/users/verification_code") >= 0 || url.indexOf("/vendors/vendors/password") >= 0){
                return chain.filter(exchange);
            }
            HttpHeaders headers = request.getHeaders();
            /**
             * Authorization 格式 type + " " + tokenValue
             * 管理端token格式："Bearer" + " " + tokenValue
             * 客户端端token格式："token" + " " + tokenValue
             */
            String authorization = headers.getFirst(AUTHORIZE_TOKEN);
            ServerHttpResponse response = exchange.getResponse();
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