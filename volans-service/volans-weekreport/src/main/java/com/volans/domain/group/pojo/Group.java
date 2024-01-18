package com.volans.domain.group.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Group implements Serializable
{
    private static final long serialVersionUID = 1L;
    // 组ID
    private Integer groupId;

    // 组名
    private String groupName;

    // 部门ID
    private Integer departmentId;

    // 状态
    private Integer state;

}
