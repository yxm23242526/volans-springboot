package com.volans.domain.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageResponseResult extends ResponseResult implements Serializable
{
    private Integer currentPage;
    private Integer size;
    private Integer total;

    public PageResponseResult(Integer currentPage, Integer size, int total)
    {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
    }
}