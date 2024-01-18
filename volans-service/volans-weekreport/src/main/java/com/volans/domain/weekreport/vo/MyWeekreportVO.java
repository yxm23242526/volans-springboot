package com.volans.domain.weekreport.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MyWeekreportVO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer userId;

    private Integer taskId;

    private Integer status;

    private String startDate;

    private String endDate;

    private List<WeekreportsVO> rows;
}
