package com.volans.domain.common.enums;

public enum HttpCodeEnum
{

    // 成功段200
    SUCCESS(200, "操作成功"),
    // 登录段1~50
    NEED_LOGIN(1, "需要登录后操作"),
    LOGIN_PASSWORD_ERROR(2, "密码错误"),
    // TOKEN50~100
    TOKEN_INVALID(50, "无效的TOKEN"),
    TOKEN_EXPIRE(51, "TOKEN已过期"),

    TOKEN_MISSING(52, "TOKEN丢失"),
    // 参数错误 500~1000
    PARAM_REQUIRE(500, "参数丢失"),
    PARAM_INVALID(501, "参数无效"),
    SERVER_ERROR(503, "服务器内部错误"),
    // 数据错误 1000~2000
    DATA_EXIST(1000, "数据已经存在"),
    DATA_NOT_EXIST(1001, "数据不存在"),
    // 权限错误 3000~3500
    NO_AUTH(3000, "权限不足");

    int code;
    String Message;

    HttpCodeEnum(int code, String Message)
    {
        this.code = code;
        this.Message = Message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return Message;
    }
}
