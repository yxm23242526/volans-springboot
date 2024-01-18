package com.volans.domain.common;


import com.volans.domain.common.enums.HttpCodeEnum;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable
{
    private Integer code;

    private String Message;

    private T data;

    public ResponseResult()
    {
        this.code = 200;
    }

    public ResponseResult(Integer code, T data)
    {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data)
    {
        this.code = code;
        this.Message = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg)
    {
        this.code = code;
        this.Message = msg;
    }

    public static ResponseResult errorResult(int code, String msg)
    {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }

    public static ResponseResult okResult(int code, String msg)
    {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }

    public static ResponseResult okResult(Object data)
    {
        ResponseResult result = setHttpCodeEnum(HttpCodeEnum.SUCCESS, HttpCodeEnum.SUCCESS.getMessage());
        if (data != null)
        {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult errorResult(HttpCodeEnum enums)
    {
        return setHttpCodeEnum(enums, enums.getMessage());
    }

    public static ResponseResult errorResult(HttpCodeEnum enums, String errorMessage)
    {
        return setHttpCodeEnum(enums, errorMessage);
    }

    public static ResponseResult setHttpCodeEnum(HttpCodeEnum enums)
    {
        return okResult(enums.getCode(), enums.getMessage());
    }

    private static ResponseResult setHttpCodeEnum(HttpCodeEnum enums, String errorMessage)
    {
        return okResult(enums.getCode(), errorMessage);
    }

    public ResponseResult<?> error(Integer code, String msg)
    {
        this.code = code;
        this.Message = msg;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data)
    {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data, String msg)
    {
        this.code = code;
        this.data = data;
        this.Message = msg;
        return this;
    }

    public ResponseResult<?> ok(T data)
    {
        this.data = data;
        return this;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String message)
    {
        this.Message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

}

