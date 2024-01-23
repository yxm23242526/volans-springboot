package com.volans.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.task.pojo.Task;
import org.springframework.stereotype.Service;

@Service
public interface TaskService extends IService<Task>
{
    public void addTask();

    public ResponseResult addTask(Task task);

    public ResponseResult getTaskListInfo();

    public ResponseResult editTask(Task task);

    public ResponseResult deleteTask(Integer taskId);
}
