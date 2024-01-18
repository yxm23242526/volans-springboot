package com.volans.controller;

import com.volans.domain.common.ResponseResult;
import com.volans.domain.project.dto.ProjectDTO;
import com.volans.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/project")
public class ProjectController
{

    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "/list")
    public ResponseResult getProjectList()
    {
        return ResponseResult.okResult(projectService.list());
    }

    @PostMapping(value = "/addProject")
    public ResponseResult addProject(@RequestBody ProjectDTO dto)
    {
        return projectService.addProject(dto);
    }

    @PostMapping(value = "/update/{projectId}")
    public ResponseResult updateProject(@PathVariable("projectId") Integer projectId, @RequestBody ProjectDTO dto)
    {
        return projectService.updateProject(projectId, dto);
    }

    @PostMapping(value = "/delete/{projectId}")
    public ResponseResult deleteProject(@PathVariable("projectId") Integer projectId)
    {
        return projectService.deleteProject(projectId);
    }

    @PostMapping(value = "/removeProjects")
    public ResponseResult deleteProjects(@RequestBody List<Integer> projectList)
    {
        return projectService.deleteProjects(projectList);
    }
}
