package com.worn.xiao.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Set;

@Component
public class TokenCheckFilter implements GlobalFilter, Ordered {

    @Value("${no.token.access.urls:/admin/login,/admin/validate/code}")
    private Set<String> noTokenAccessUrls;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(allowAccess(exchange)){
            return chain.filter(exchange);
        }

        String token = getToken(exchange);
        if(StringUtils.isBlank(token)){ //如果token不存在
            return buildUnAuthSuccess(exchange);
        }
        Boolean hasKey = redisTemplate.hasKey(token);
        if (hasKey==null || !hasKey) { //验证token是否正确
            return buildUnAuthSuccess(exchange);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> buildUnAuthSuccess(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", "unauthorized");
        jsonObject.put("error_description", "invalid_token");
        DataBuffer dataBuffer = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes());
        return response.writeWith(Flux.just(dataBuffer));
    }

    private String getToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorization) || authorization.trim().isEmpty()) {
            return null;
        }
        return authorization.replace("bearer ", "");
    }

    private boolean allowAccess(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        if (noTokenAccessUrls.contains(path)) {
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
