package com.volans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.volans.domain.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    @Select("SELECT user_id from user")
    public List<Integer> getAllUserIdList();

    @Select("SELECT user_id FROM user WHERE status = 1")
    public List<Integer> getActiveUserIdList();
}
