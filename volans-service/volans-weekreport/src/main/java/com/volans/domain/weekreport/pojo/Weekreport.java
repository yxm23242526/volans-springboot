package com.volans.domain.weekreport.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Weekreport implements Serializable
{
    private static final long serialVersionUID = 1L;
    // 工时记录ID
    @TableId(value = "weekreport_id", type = IdType.AUTO)
    private Integer weekreportId;

    // 项目ID
    private Integer projectId;

    // 用户ID
    private Integer userId;

    // 工时日期
    private String date;

    // 工时
    private Integer workTime;

    // 工作内容
    private String workContent;

    // 状态
    private Integer status;

    private Integer taskId;

    public Weekreport()
    {
    }

    public Weekreport(Integer userId, Integer taskId, Integer status)
    {
        this.userId = userId;
        this.taskId = taskId;
        this.status = status;
    }
}
