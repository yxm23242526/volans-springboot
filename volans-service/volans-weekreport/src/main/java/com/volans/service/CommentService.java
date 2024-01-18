package com.volans.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.volans.domain.comment.dto.CommentDTO;
import com.volans.domain.comment.pojo.Comment;
import com.volans.domain.common.ResponseResult;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface CommentService extends IService<Comment>
{
    public ResponseResult getCommentList() throws ParseException;

    ResponseResult addComment(CommentDTO commentDTO);
}
