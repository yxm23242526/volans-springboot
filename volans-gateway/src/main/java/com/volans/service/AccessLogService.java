package com.volans.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.volans.domain.GatewayLog;
import org.springframework.stereotype.Service;

@Service
public interface AccessLogService extends IService<GatewayLog>
{
    /**
     * 保存AccessLog
     *
     * @param gatewayLog 请求响应日志
     * @return 响应日志
     */
    void saveAccessLog(GatewayLog gatewayLog);
}