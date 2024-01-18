package com.volans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volans.domain.comment.dto.CommentDTO;
import com.volans.domain.comment.pojo.Comment;
import com.volans.domain.comment.vo.CommentVO;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.common.enums.HttpCodeEnum;
import com.volans.mapper.CommentMapper;
import com.volans.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService
{
    @Autowired
    private CommentMapper commentMapper;

    public ResponseResult getCommentList() throws ParseException
    {
        List<CommentVO> commentList = commentMapper.getCommentList();
        return ResponseResult.okResult(commentList);
    }

    @Override
    public ResponseResult addComment(CommentDTO commentDTO)
    {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        save(comment);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }
}
