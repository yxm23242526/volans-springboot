package com.volans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.volans.domain.task.pojo.Task;
import com.volans.domain.task.vo.TaskInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task>
{
    public List<TaskInfoVO> selectTaskInfo();
}
