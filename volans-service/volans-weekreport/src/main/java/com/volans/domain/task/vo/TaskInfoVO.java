package com.volans.domain.task.vo;

import com.volans.domain.user.vo.UserInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TaskInfoVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer taskId;

    private String startDate;

    private String endDate;

    private Integer unSubmitUserCount;

    private String unSubmitUserName;
}
