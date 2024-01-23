package com.volans.domain.common;


import com.volans.domain.common.enums.HttpCodeEnum;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable
{
    private Integer code;

    private String message;

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

    public ResponseResult(Integer code, String msg)
    {
        this.code = code;
        this.message = msg;
    }

    public ResponseResult(HttpCodeEnum httpCodeEnum)
    {
        this.code = httpCodeEnum.getCode();
        this.message = httpCodeEnum.getMessage();
    }

    public ResponseResult(Integer code, String msg, T data)
    {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseResult(HttpCodeEnum httpCodeEnum, T data)
    {
        this.code = httpCodeEnum.getCode();
        this.message = httpCodeEnum.getMessage();
        this.data = data;
    }

    public static ResponseResult okResult(Object data)
    {
        return new ResponseResult(HttpCodeEnum.SUCCESS, data);
    }

    public static ResponseResult errorResult(int code, String msg)
    {
        return new ResponseResult(code, msg);
    }

    public static ResponseResult errorResult(HttpCodeEnum httpCodeEnum)
    {
        return new ResponseResult(httpCodeEnum);
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
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
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

