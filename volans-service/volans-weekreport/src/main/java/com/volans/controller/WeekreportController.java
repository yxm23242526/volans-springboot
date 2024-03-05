package com.volans.controller;

import com.volans.domain.common.ResponseResult;
import com.volans.domain.task.dto.SpecifiedReportsDTO;
import com.volans.domain.weekreport.dto.QueryDTO;
import com.volans.domain.weekreport.pojo.Weekreport;
import com.volans.service.WeekreportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/weekreport")
public class WeekreportController
{
    @Autowired
    private WeekreportService weekreportService;

    /**
     * 获取我的周报
     *
     * @param userId 用户ID
     * @return 周报信息的ResponseResult对象
     */
    @GetMapping(value = "/getWeekreport")
    public ResponseResult getMyWeekreport(Integer userId)
    {
        return weekreportService.getMyWeekreport(userId);
    }

    @PostMapping(value = "/submit/{userId}")
    public ResponseResult submitWeekreport(@RequestHeader("userId") Integer managerUserId, @RequestBody List<Weekreport> list, @PathVariable("userId") Integer userId)
    {
        return weekreportService.submitWeekreport(managerUserId, list, userId);
    }

    @PostMapping(value = "/revokeWeekreport/{taskId}/{userId}")
    public ResponseResult revokeWeekreport(@RequestHeader("userId") Integer managerUserId, @PathVariable("taskId") Integer taskId, @PathVariable("userId") Integer userId)
    {
        return weekreportService.revokeWeekreport(managerUserId, taskId, userId);
    }

    @PostMapping (value = "/export/query")
    public ResponseResult doQuery(@RequestHeader("userId") Integer userId, @RequestBody QueryDTO queryDto) throws IOException
    {
        return weekreportService.exportQuery(userId, queryDto, false);
    }

    @PostMapping(value = "/export")
    public ResponseResult export(@RequestHeader("userId") Integer userId, @RequestBody QueryDTO queryDto) throws IOException
    {
        return weekreportService.exportQuery(userId, queryDto, true);
    }

    @PostMapping(value = "/exportUser/{taskId}")
    public ResponseResult exportUser(@RequestHeader("userId") Integer userId,  @PathVariable("taskId") Integer taskId) throws IOException
    {
        return weekreportService.exportUser(userId, taskId);
    }

    @PostMapping(value = "/addWeekreport")
    public ResponseResult addWeekreport(@RequestBody Weekreport weekreport)
    {
        return weekreportService.addWeekreport(weekreport);
    }

    @PostMapping(value = "/createNewWeekreportForTask")
    public ResponseResult createNewWeekreportForTask(@RequestBody List<Weekreport> list)
    {
        return weekreportService.createNewWeekreportForTask(list);
    }

    @PostMapping(value = "/getSpecifiedReports")
    public ResponseResult getSpecifiedReports(@RequestBody SpecifiedReportsDTO dto)
    {
        return weekreportService.getSpecifiedReports(dto);
    }
}
