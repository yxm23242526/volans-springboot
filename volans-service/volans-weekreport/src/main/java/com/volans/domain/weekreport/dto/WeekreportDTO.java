package com.volans.domain.weekreport.dto;

import com.volans.domain.weekreport.pojo.Weekreport;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WeekreportDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<Weekreport> list;
}
