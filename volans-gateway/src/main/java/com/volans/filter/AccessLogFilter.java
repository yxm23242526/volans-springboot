package com.volans.filter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.volans.domain.GatewayLog;
import com.volans.domain.ResponseResult;
import com.volans.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日志过滤器，用于记录日志
 *
 * @author jianzh5
 * @date 2020/3/24 17:17
 */
@Slf4j
@Component
public class AccessLogFilter implements GlobalFilter, Ordered
{
    @Autowired
    private AccessLogService accessLogService;

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();


    /**
     * 顺序必须是<-1，否则标准的NettyWriteResponseFilter将在您的过滤器得到一个被调用的机会之前发送响应
     * 也就是说如果不小于 -1 ，将不会执行获取后端响应的逻辑
     *
     * @return
     */
    @Override
    public int getOrder()
    {
        return -100;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {

        ServerHttpRequest request = exchange.getRequest();

        // 请求路径
        String requestPath = request.getPath().pathWithinApplication().value();

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

        GatewayLog gatewayLog = new GatewayLog();
        gatewayLog.setRequestMethod(request.getMethod().name());
        gatewayLog.setRequestPath(requestPath);
        gatewayLog.setTargetServer(route.getId());
        gatewayLog.setRequestTime(new Date());
        gatewayLog.setIp(String.valueOf(request.getRemoteAddress()).replace("/", ""));


//        MediaType mediaType = request.getHeaders().getContentType();
//
//        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType) || MediaType.APPLICATION_JSON.isCompatibleWith(mediaType))
//        {
//            return writeBodyLog(exchange, chain, gatewayLog);
//        }
//        else
//        {
//            return writeBasicLog(exchange, chain, gatewayLog);
//        }
        StringBuilder builder = new StringBuilder();
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            builder.append(entry.getKey()).append("=").append(StringUtils.join(entry.getValue(), ","));
        }
//        gatewayLog.setRequestBody(builder.toString());
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);
        accessLogService.save(gatewayLog);
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    /**
     * 记录响应日志
     * 通过 DataBufferFactory 解决响应体分段传输问题。
     */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange, GatewayLog gatewayLog)
    {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        return new ServerHttpResponseDecorator(response)
        {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body)
            {
                if (body instanceof Flux)
                {
                    Date responseTime = new Date();
                    gatewayLog.setResponseTime(responseTime);
                    // 计算执行时间
                    long executeTime = (responseTime.getTime() - gatewayLog.getRequestTime().getTime());

                    gatewayLog.setExecuteTime(executeTime);

                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);


                    if (ObjectUtil.equal(this.getStatusCode(), HttpStatus.OK)
                            && StringUtils.isNotBlank(originalResponseContentType)
                            && originalResponseContentType.contains("application/json"))
                    {

                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);

                            // 释放掉内存
                            DataBufferUtils.release(join);
                            ResponseResult result = JacksonUtils.toObj(content, ResponseResult.class);
                            gatewayLog.setResponseCode(result.getCode());
                            gatewayLog.setResponseData(result.getData().toString());
                            accessLogService.updateById(gatewayLog);
                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                accessLogService.updateById(gatewayLog);
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }
}