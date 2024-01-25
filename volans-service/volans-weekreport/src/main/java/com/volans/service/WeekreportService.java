package com.volans.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.task.dto.SpecifiedReportsDTO;
import com.volans.domain.weekreport.dto.QueryDTO;
import com.volans.domain.weekreport.pojo.Weekreport;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface WeekreportService extends IService<Weekreport>
{
    public ResponseResult getMyWeekreport(Integer userId);

    public ResponseResult exportQuery(Integer userId, QueryDTO queryDto, boolean isExport) throws IOException;

    public ResponseResult submitWeekreport(Integer managerUserId, List<Weekreport> list, Integer userId);

    public ResponseResult revokeWeekreport(Integer managerUserId, Integer taskId, Integer userId);

    public ResponseResult addWeekreport(Weekreport weekreport);

    public ResponseResult createNewWeekreportForTask(List<Weekreport> list);

    public ResponseResult exportUser(Integer userId, Integer taskId) throws IOException;

    public ResponseResult getSpecifiedReports(SpecifiedReportsDTO dto);
}
