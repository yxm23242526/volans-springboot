package com.volans.domain.comment.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentVO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer commentId;

    private Integer userId;

    private String nickName;

    private String image;

    private String commentContent;

    private String commentTime;
}
