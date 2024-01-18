package com.volans.domain.weekreport.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExportModelTwoVO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Excel(name = "序号", type = 10, width = 10)
    private Integer number;

    @Excel(name = "项目名称", mergeVertical = true, width = 20)
    private String projectName;

    @Excel(name = "日期", type = 10, width = 10)
    private String date;

    @Excel(name = "员工", type = 10, width = 10)
    private String userName;

    @Excel(name = "工时", type = 10, width = 15)
    private String workTime;

    @Excel(name = "工作内容", type = 10, width = 15)
    private String workContent;
}
