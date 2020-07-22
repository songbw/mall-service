package com.fengchao.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author songbw
 * @date 2020/7/22 10:38
 */
@Slf4j
public class RemoteAddrKeyResolver implements KeyResolver {

    public static final String BEAN_NAME = "remoteAddrKeyResolver";

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        log.info("Remote addr key is : {}", exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
