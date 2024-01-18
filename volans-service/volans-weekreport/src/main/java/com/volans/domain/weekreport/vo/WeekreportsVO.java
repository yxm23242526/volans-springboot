package com.volans.domain.weekreport.vo;

import com.volans.domain.weekreport.pojo.Weekreport;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WeekreportsVO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String curDate;

    private List<Weekreport> content;
}
