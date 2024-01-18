package com.volans.domain.project.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String projectName;
}
