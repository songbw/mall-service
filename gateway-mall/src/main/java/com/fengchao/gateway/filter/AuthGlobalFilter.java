package com.fengchao.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.gateway.bean.OperaResponse;
import com.fengchao.gateway.utils.JwtHelper;
import com.fengchao.gateway.utils.RedisDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisDAO redisDAO;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        OperaResponse operaResponse = new OperaResponse();
        String url = exchange.getRequest().getURI().getPath();
        //忽略以下url请求
        if(url.indexOf("/toushi/") >= 0 || url.indexOf("login") >= 0 || url.indexOf("/thirdParty/token") >= 0 || url.indexOf("/thirdLogin") >= 0 || url.indexOf("/v2/vendors/vendors") >= 0 || url.indexOf("/users/verification_code") >= 0 || url.indexOf("/vendors/vendors/password") >= 0){
            return chain.filter(exchange);
        }
        //从请求头中取得token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(StringUtils.isEmpty(token)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            operaResponse.setCode(401);
            operaResponse.setMsg("验证token失败。");
            return exchange.getResponse().writeWith(Flux.just(this.getBodyBuffer(exchange.getResponse(), operaResponse)));
        }
        //请求中的token是否在redis中存在
        String verifyResult = redisDAO.getValue(token) ;
        String subject = JwtHelper.getValue(token);
        if (verifyResult == null || "".equals(verifyResult) || subject == null || "".equals(subject)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            operaResponse.setCode(401);
            operaResponse.setMsg("验证token失败。");
            return exchange.getResponse().writeWith(Flux.just(this.getBodyBuffer(exchange.getResponse(), operaResponse)));
        }
        if (!verifyResult.equals(subject)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            operaResponse.setCode(401);
            operaResponse.setMsg("验证token失败。");
            return exchange.getResponse().writeWith(Flux.just(this.getBodyBuffer(exchange.getResponse(), operaResponse)));
        }
        return chain.filter(exchange);
    }

    /**
     * 封装返回值
     *
     * @param response
     * @param result
     * @return
     */
    private DataBuffer getBodyBuffer(ServerHttpResponse response, OperaResponse result) {
        return response.bufferFactory().wrap(JSONObject.toJSONBytes(result));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
