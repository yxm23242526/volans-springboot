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
    // 请求数据错误 1000~2000
    PARAM_REQUIRE(1000, "参数丢失"),
    PARAM_INVALID(1001, "参数无效"),
    SERVER_ERROR(1002, "服务器内部错误"),

    // 服务器数据错误 2000~10000
    // 增加操作错误 2000~3000
    DATA_EXIST(2000, "数据已经存在"),
    DATA_NOT_EXIST(2001, "数据不存在"),
    // 修改操作错误 3000~4000

    // 删除操作错误 4000~5000
    DATA_HAS_RELATION(4000, "存在关联数据，无法删除"),

    // 权限错误 10000~12000
    NO_AUTH(10000, "权限不足");

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
