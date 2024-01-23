package com.volans.domain.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String userName;
}
