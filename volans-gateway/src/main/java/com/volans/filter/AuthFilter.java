package com.volans.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.volans.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter,Ordered
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        //1.获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //2.判断是否是登录
        if (request.getURI().getPath().contains("/login") || request.getURI().getPath().contains("/task/addTask"))
        {
            //放行
            return chain.filter(exchange);
        }

//        3.获取token，如果获取不到，直接返回401
//        String authorizationHeader = request.getHeaders().getFirst("Authorization");
//        if (!authorizationHeader.startsWith("Bearer "))
//        {
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
        String token = request.getHeaders().getFirst("token").substring("Bearer ".length());
        if (StringUtils.isBlank(token))
        {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //4.校验token，如果token过期或者非法，直接返回401
        Claims claimsBody;
        try
        {
            claimsBody = AppJwtUtil.getClaimsBody(token);
            //是否是过期
            int result = AppJwtUtil.verifyToken(claimsBody);
            if (result == 1 || result == 2)
            {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //5.解析token，获取用户id，放到请求头上，放行请求（将来微服务里可以直接获取当前用户id）
        Object userId = claimsBody.get("id");
        ServerHttpRequest newRequest = request.mutate()
                .headers(httpHeaders -> httpHeaders.add("userId", userId.toString()))
                .build();
        exchange.mutate().request(newRequest).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder()
    {
        return 0;
    }
}
