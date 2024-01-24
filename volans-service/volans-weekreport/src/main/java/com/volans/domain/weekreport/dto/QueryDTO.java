package com.volans.domain.weekreport.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class QueryDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer model;
    private Integer[] userId;
    private Integer[] projectId;
    private LocalDate[] date;
}
