package com.volans.domain.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer departmentId;

    private Integer groupId;

    private Integer identityId;

    private Integer status;

    private Integer page;

    private Integer pageSize;

    public QueryDTO(Integer status)
    {
        setStatus(status);
    }
}
