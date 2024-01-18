package com.volans.domain.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class GatewayLog {

    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;
    /**访问实例*/
    @TableField(value = "target_server")
    private String targetServer;
    /**请求路径*/
    @TableField(value = "request_path")
    private String requestPath;
    /**请求方法*/
    @TableField(value = "request_method")
    private String requestMethod;
    /**请求体*/
    @TableField(value = "request_body")
    private String requestBody;
    /**响应体*/
    @TableField(value = "response_code")
    private Integer responseCode;
    /**响应体*/
    @TableField(value = "response_data")
    private String responseData;
    /**请求ip*/
    @TableField(value = "ip")
    private String ip;
    /**请求时间*/
    @TableField(value = "request_time")
    private Date requestTime;
    /**响应时间*/
    @TableField(value = "response_time")
    private Date responseTime;
    /**执行时间*/
    @TableField(value = "execute_time")
    private Long executeTime;
}
