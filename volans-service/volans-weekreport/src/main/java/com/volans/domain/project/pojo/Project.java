package com.volans.domain.project.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Project implements Serializable
{
    private static final long serialVersionUID = 1L;
    // 项目ID
    @TableId(value = "project_id", type = IdType.AUTO)
    private Integer projectId;

    // 项目名称
    private String projectName;

    // 部门ID
    private Integer departmentId;

    private LocalDate startTime;

    private LocalDate endTime;

    // 状态
    private Integer status;
}
