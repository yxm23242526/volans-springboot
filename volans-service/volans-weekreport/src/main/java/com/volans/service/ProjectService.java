package com.volans.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.project.dto.ProjectDTO;
import com.volans.domain.project.pojo.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService extends IService<Project>
{
    public ResponseResult addProject(ProjectDTO dto);

    public ResponseResult updateProject(Integer projectId, ProjectDTO dto);

    public ResponseResult deleteProject(Integer projectId);

    public ResponseResult deleteProjects(List<Integer> projectList);
}
