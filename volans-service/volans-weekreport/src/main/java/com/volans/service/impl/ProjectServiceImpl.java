package com.volans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.common.enums.HttpCodeEnum;
import com.volans.domain.project.dto.ProjectDTO;
import com.volans.domain.project.pojo.Project;
import com.volans.mapper.ProjectMapper;
import com.volans.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService
{

    @Autowired
    private ProjectMapper projectMapper;

    public ResponseResult addProject(ProjectDTO dto)
    {
        // 1. 参数校验
        if (dto == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }
        if(lambdaQuery().eq(dto.getProjectName() != null, Project::getProjectName, dto.getProjectName()).exists())
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_EXIST);
        }
        Project project = new Project();
        BeanUtils.copyProperties(dto, project);
        project.setStatus(1);
        save(project);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    public ResponseResult updateProject(Integer projectId, ProjectDTO dto)
    {
        // 1. 参数校验
        if (projectId == null || projectId <= 0 || dto == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }

        // 2.
        Project project = getById(projectId);
        if (project == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
        }
        BeanUtils.copyProperties(dto, project);
        updateById(project);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    public ResponseResult deleteProject(Integer projectId)
    {
        Project project = getById(projectId);
        if (project == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
        }
        project.setStatus(2);
        updateById(project);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    public ResponseResult deleteProjects(List<Integer> projectList)
    {
        // 1. 参数校验
        if (projectList == null || projectList.isEmpty())
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }
        removeBatchByIds(projectList);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }
}
