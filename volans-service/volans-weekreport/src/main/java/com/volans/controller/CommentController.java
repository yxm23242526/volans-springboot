package com.volans.controller;

import com.volans.domain.comment.dto.CommentDTO;
import com.volans.domain.common.ResponseResult;
import com.volans.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/comment")
public class CommentController
{
    @Autowired
    private CommentService commentService;

    @GetMapping("/getAllComments")
    public ResponseResult getCommentList() throws ParseException
    {
        return commentService.getCommentList();
    }

    @PostMapping("/addComment")
    public ResponseResult addComment(@RequestBody CommentDTO commentDTO)
    {
        return commentService.addComment(commentDTO);
    }
}
