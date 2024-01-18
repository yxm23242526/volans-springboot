package com.volans.domain.department.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Department implements Serializable
{
    private static final long serialVersionUID = 1L;
    // 部门ID
    private Integer departmentId;

    // 部门名称
    private String departmentName;

    // 状态
    private Integer state;
}
