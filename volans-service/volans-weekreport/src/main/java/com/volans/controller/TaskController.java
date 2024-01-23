package com.volans.controller;

import com.volans.domain.common.ResponseResult;
import com.volans.domain.common.enums.HttpCodeEnum;
import com.volans.domain.task.pojo.Task;
import com.volans.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/task")
public class TaskController
{
    @Autowired
    private TaskService taskService;

    @GetMapping(value = "/{id}")
    public Task getTaskById(@PathVariable("id") Integer id)
    {
        return taskService.getById(id);
    }

    @PostMapping(value = "/addTask2")
    public void addTask()
    {
        taskService.addTask();
    }

    @PostMapping(value = "/addTask")
    public ResponseResult addTask(@RequestBody Task task)
    {
        return taskService.addTask(task);
    }

    @GetMapping(value = "/list")
    public ResponseResult getTaskList()
    {
        return taskService.getTaskListInfo();
    }

    @PostMapping(value = "/editTask")
    public ResponseResult editTask(@RequestBody Task task)
    {
        return taskService.editTask(task);
    }

    @DeleteMapping(value = "/deleteTask/{taskId}")
    public ResponseResult deleteTask(@PathVariable("taskId") Integer taskId)
    {
        return taskService.deleteTask(taskId);
    }
}
