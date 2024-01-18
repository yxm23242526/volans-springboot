package com.volans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.volans.domain.task.pojo.Task;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskMapper extends BaseMapper<Task>
{
}
