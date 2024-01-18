package com.volans.domain.comment.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Comment implements Serializable
{
    private static final long serialVersionUID = 1L;
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    private Integer userId;

    private String commentContent;

    private Integer likeCount;

    private Integer dislikeCount;

    private Boolean isReply;

    private Integer replyId;

    private Boolean isDelete;

    private String commentTime;
}
