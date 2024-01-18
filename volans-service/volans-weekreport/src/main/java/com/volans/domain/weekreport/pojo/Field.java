package com.volans.domain.weekreport.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Field implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer fieldId;

    private String fieldName;

    public Field(Integer fieldId, String fieldName)
    {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
    }

    public static List<Field> getFieldList()
    {
        List<Field> fieldList = new ArrayList<>();
        fieldList.add(new Field(0, "员工"));
        fieldList.add(new Field(1, "工时"));
        fieldList.add(new Field(2, "工作内容"));
        return fieldList;
    }
}
