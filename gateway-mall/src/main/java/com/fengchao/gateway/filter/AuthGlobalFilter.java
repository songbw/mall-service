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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String url = exchange.getRequest().getURI().getPath();
        //忽略以下url请求
        if(url.indexOf("/") >= 0){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
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
