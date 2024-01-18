package com.volans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.volans.domain.comment.pojo.Comment;
import com.volans.domain.comment.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment>
{
    public List<CommentVO> getCommentList();
}
