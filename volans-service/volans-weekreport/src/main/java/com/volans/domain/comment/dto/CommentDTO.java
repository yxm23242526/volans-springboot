package com.volans.domain.comment.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String commentContent;
    private String commentTime;
}
