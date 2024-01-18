package com.volans.domain.task.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable
{
    private static final long serialVersionUID = 1L;
    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    private String startDate;

    private String endDate;
}
