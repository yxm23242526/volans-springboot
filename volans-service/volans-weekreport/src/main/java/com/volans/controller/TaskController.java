package com.volans.controller;

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

    @GetMapping(value = "/addTask")
    public void addTask()
    {
        taskService.addTask();
    }
}
