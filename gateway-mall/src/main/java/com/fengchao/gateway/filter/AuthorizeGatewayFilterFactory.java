package com.fengchao.gateway.filter;

import com.fengchao.gateway.config.GatewayConfig;
import com.fengchao.gateway.utils.JwtHelper;
import com.fengchao.gateway.utils.RedisDAO;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@EnableConfigurationProperties({GatewayConfig.class})
@Slf4j
//@Component
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
            log.info("请求token: {}", authorization);
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

            String method = request.getMethodValue();
            log.info("请求方法：{}", method);
            if ("POST".equals(method) || "PUT".equals(method)) {
//                //从请求里获取Post请求体
//                String bodyStr = resolveBodyFromRequest(request);
//                if (bodyStr == null) {
//                    bodyStr = "" ;
//                }
//                // 得到Post请求的请求参数后，做你想做的事
//                log.info("请求参数：{}", bodyStr);
//                //下面的将请求体再次封装写回到request里，传到下一级，否则，由于请求体已被消费，后续的服务将取不到值
//                URI uri = request.getURI();
//                ServerHttpRequest requestChain = request.mutate().uri(uri).build();
//                DataBuffer bodyDataBuffer = stringBuffer(bodyStr);
//                Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);
//
//                requestChain = new ServerHttpRequestDecorator(requestChain) {
//                    @Override
//                    public Flux<DataBuffer> getBody() {
//                        return bodyFlux;
//                    }
//                };
                //封装request，传给下一级
                ServerRequest serverRequest = new DefaultServerRequest(exchange);
                // mediaType
                MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
                Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                        .flatMap(body -> {
                            log.info("请求参数：{}", body);
//                            if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
//
//                                // origin body map
//                                Map<String, Object> bodyMap = decodeBody(body);
//
//                                // TODO decrypt & auth
//
//                                // new body map
//                                Map<String, Object> newBodyMap = new HashMap<>();
//
//                                return Mono.just(encodeBody(newBodyMap));
//                            }
                            return Mono.just(body);
                        });
                BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
                CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
                return bodyInserter.insert(outputMessage,  new BodyInserterContext())
                        .then(Mono.defer(() -> {
                            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                                    exchange.getRequest()) {
                                @Override
                                public HttpHeaders getHeaders() {
                                    long contentLength = headers.getContentLength();
                                    HttpHeaders httpHeaders = new HttpHeaders();
                                    httpHeaders.putAll(super.getHeaders());
                                    if (contentLength > 0) {
                                        httpHeaders.setContentLength(contentLength);
                                    } else {
                                        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                                    }
                                    return httpHeaders;
                                }

                                @Override
                                public Flux<DataBuffer> getBody() {
                                    return outputMessage.getBody();
                                }
                            };
                            return chain.filter(exchange.mutate().request(decorator).build());
                        }));
            } else if ("GET".equals(method) || "DELETE".equals(method)) {
                MultiValueMap<String, String> requestQueryParams = request.getQueryParams();
                // 得到Get请求的请求参数后，做你想做的事
                StringBuilder builder = new StringBuilder("");
                for (Map.Entry<String, List<String>> entry : requestQueryParams.entrySet()) {
                    builder.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
                }
                log.info("请求参数：{}", builder.toString());
                return chain.filter(exchange);

            }
//            if ("Bearer".equals(type)) {
//                exchange.getRequest().getHeaders().add("username", jwtValue);
////                headers.add("username", jwtValue);
//            }
            return chain.filter(exchange);
        };
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();

        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }

    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
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
