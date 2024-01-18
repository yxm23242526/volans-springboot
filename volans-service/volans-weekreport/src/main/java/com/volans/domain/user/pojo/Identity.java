package com.volans.domain.user.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Identity implements Serializable
{
    private static final long serialVersionUID = 1L;

    // 身份ID
    private Integer identityId;

    // 身份名称
    private String identity_name;
}
