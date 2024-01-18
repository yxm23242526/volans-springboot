package com.volans.domain.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String password;
}
