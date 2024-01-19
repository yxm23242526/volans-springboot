package com.volans.domain.weekreport.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExportModelUserVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "日期", type = 10, width = 15, mergeVertical = true)
    private String date;

    @Excel(name = "项目名称", width = 20)
    private String projectName;

    @Excel(name = "工作内容", type = 10, width = 50)
    private String workContent;

    @Excel(name = "工时", type = 10, width = 10)
    private String workTime;
}
