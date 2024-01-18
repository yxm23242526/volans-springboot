package com.volans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volans.domain.GatewayLog;
import com.volans.mapper.LogMapper;
import com.volans.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessLogServiceImpl extends ServiceImpl<LogMapper, GatewayLog> implements AccessLogService
{
    @Autowired
    private LogMapper logMapper;

    @Override
    public void saveAccessLog(GatewayLog gatewayLog)
    {
        saveAccessLog(gatewayLog);
    }
}