package com.volans.domain.common;

import lombok.Data;

@Data
public class PageRequestDTO
{

    protected Integer size;
    protected Integer page;

    public void checkParam()
    {
        if (this.page == null || this.page < 0)
        {
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100)
        {
            setSize(10);
        }
    }
}