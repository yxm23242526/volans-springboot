package com.volans.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.common.enums.HttpCodeEnum;
import com.volans.domain.consts.DateConst;
import com.volans.domain.consts.IdentityConst;
import com.volans.domain.consts.StatusConst;
import com.volans.domain.task.dto.SpecifiedReportsDTO;
import com.volans.domain.task.pojo.Task;
import com.volans.domain.weekreport.dto.QueryDTO;
import com.volans.domain.weekreport.pojo.Weekreport;
import com.volans.domain.weekreport.vo.*;
import com.volans.mapper.TaskMapper;
import com.volans.mapper.UserMapper;
import com.volans.mapper.WeekreportMapper;
import com.volans.service.MinIOService;
import com.volans.service.WeekreportService;
import com.volans.utils.DateFormatUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class WeekreportServiceImpl extends ServiceImpl<WeekreportMapper, Weekreport> implements WeekreportService
{

    @Autowired
    private WeekreportMapper weekreportMapper;

    @Autowired
    private MinIOService minioService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult getMyWeekreport(Integer userId)
    {
        List<Integer> taskList = weekreportMapper.getTaskList(userId);
        if (taskList.isEmpty())
        {
            return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
        }
        List<MyWeekreportVO> result = new ArrayList<>();
        Map<Integer, Map<String, List<Weekreport>>> dateTaskMap = new LinkedHashMap<>();

        // 缓存每一天的数据
        for (Integer taskId : taskList)
        {
            List<String> dateList = weekreportMapper.getDateListByUserIdAndTaskId(userId, taskId);
            for (String date : dateList)
            {
                List<Weekreport> weekreportList = weekreportMapper.getWeekreportListByUserIdAndTaskIdAndDate(userId, taskId, date);
                if (!dateTaskMap.containsKey(taskId))
                {
                    dateTaskMap.put(taskId, new LinkedHashMap<>());
                }
                if (!dateTaskMap.get(taskId).containsKey(date))
                {
                    dateTaskMap.get(taskId).put(date, weekreportList);
                }
            }
        }

        // 构建MyWeekreportVo
        for (Integer taskId : taskList)
        {
            MyWeekreportVO myWeekreportVo = new MyWeekreportVO();
            myWeekreportVo.setUserId(userId);
            myWeekreportVo.setTaskId(taskId);
            Task task = taskMapper.selectById(taskId);
            myWeekreportVo.setStartDate(task.getStartDate());
            myWeekreportVo.setEndDate(task.getEndDate());

            List<WeekreportsVO> rows = new ArrayList<>();
            Map<String, List<Weekreport>> dateTaskReportsMap = dateTaskMap.get(taskId);
            for (String date : dateTaskReportsMap.keySet())
            {
                List<Weekreport> weekreportList = dateTaskReportsMap.get(date);
                WeekreportsVO weekreportsVo = new WeekreportsVO();
                weekreportsVo.setCurDate(date);
                weekreportsVo.setContent(weekreportList);
                if (!weekreportList.isEmpty())
                {
                    myWeekreportVo.setStatus(weekreportList.get(0).getStatus());
                    rows.add(weekreportsVo);
                }
            }
            myWeekreportVo.setRows(rows);
            result.add(myWeekreportVo);
        }
        return ResponseResult.okResult(result);
    }

    public ResponseResult exportQuery(Integer userId, QueryDTO queryDto, boolean isExport) throws IOException
    {
        if (queryDto.getModel() == 1)
        {
            List<ExportModelOneVO> exportList = weekreportMapper.getExportModelOneList(queryDto);
            int count = 1;
            List<Integer> index = new ArrayList<>();
            List<ExportModelOneVO> addList = new ArrayList<>();
            for (ExportModelOneVO exportVO : exportList)
            {
                if (exportVO != null)
                {
                    if (exportVO != null)
                    {
                        // TODO 暂时先这么处理，后面考虑用一个列表或者在数据库增加优先级字段来处理某些项目前置的效果
                        if (exportVO.getProjectId() != null && exportVO.getProjectId() == 8)
                        {
                            index.add(0, count - 1);
                        }
                        count++;
                    }
                }
            }
            for (int i : index)
            {
                addList.add(0, exportList.get(i));
                exportList.remove(i);
            }
            if (!addList.isEmpty())
            {
                exportList.addAll(0, addList);
            }
            if (isExport)
            {
                ExportParams exportParams = new ExportParams("周报导出", "周报");
                Workbook sheets = ExcelExportUtil.exportExcel(exportParams, ExportModelOneVO.class, exportList);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                sheets.write(outputStream);
                outputStream.flush();
                byte[] content = outputStream.toByteArray();
                InputStream is = new ByteArrayInputStream(content);
                String url = minioService.uploadExcelFile(String.valueOf(userId), queryDto.getDate() == null ? "周报.xlsx" : queryDto.getDate()[0] + "_" + queryDto.getDate()[1] + ".xlsx", is);
                return ResponseResult.okResult(url);
            }
            return ResponseResult.okResult(exportList);
        }
        else
        {
            List<ExportModelTwoVO> exportList = weekreportMapper.getExportModelTwoList(queryDto);
            int count = 1;
            List<Integer> index = new ArrayList<>();
            List<ExportModelTwoVO> addList = new ArrayList<>();
            for (ExportModelTwoVO exportVO : exportList)
            {
                if (exportVO != null)
                {
                    // TODO 暂时先这么处理，后面考虑用一个列表或者在数据库增加优先级字段来处理某些项目前置的效果
                    if (exportVO.getProjectId() != null && exportVO.getProjectId() == 8)
                    {
                        index.add(0, count - 1);
                    }
                    count++;
                }
            }
            for (int i : index)
            {
                addList.add(0, exportList.get(i));
                exportList.remove(i);
            }
            if (!addList.isEmpty())
            {
                exportList.addAll(0, addList);
            }
            if (isExport)
            {
                ExportParams exportParams = new ExportParams("周报导出", "周报");
                Workbook sheets = ExcelExportUtil.exportExcel(exportParams, ExportModelTwoVO.class, exportList);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                sheets.write(outputStream);
                outputStream.flush();
                byte[] content = outputStream.toByteArray();
                InputStream is = new ByteArrayInputStream(content);
                String url = minioService.uploadExcelFile(String.valueOf(userId) + "/report", queryDto.getDate() == null ? "周报.xlsx" : queryDto.getDate()[0] + "_" + queryDto.getDate()[1] + ".xlsx", is);
                return ResponseResult.okResult(url);
            }
            return ResponseResult.okResult(exportList);
        }
    }

    @Override
    public ResponseResult submitWeekreport(Integer managerUserId, List<Weekreport> list, Integer userId)
    {
        // 1. 校验参数
        if (list == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID);
        }
        if (userId == -1)
        {
            userId = managerUserId;
        }
        else
        {
            Integer identityId = userMapper.selectById(managerUserId).getIdentityId();
            if (identityId < IdentityConst.TEAM_LEADER)
            {
                return ResponseResult.errorResult(HttpCodeEnum.NO_AUTH);
            }
        }
        // 2. 处理数据
        Integer taskId = list.get(0).getTaskId();
        List<Integer> weekreportIdList = weekreportMapper.getWeekreportIdListByUserIdAndTaskId(userId, taskId);
        for (Weekreport weekreport : list)
        {
            if (weekreport.getWeekreportId() != null && weekreportIdList.contains(weekreport.getWeekreportId()))
            {
                weekreportIdList.remove(weekreport.getWeekreportId());
            }
            weekreport.setStatus(StatusConst.WEEKREPORT_COMMITTED);
            weekreport.setTaskId(taskId);
            weekreport.setUserId(userId);
        }
        // 3. 数据持久化
        if(!weekreportIdList.isEmpty())
        {
            removeBatchByIds(weekreportIdList);
        }
        saveOrUpdateBatch(list);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult revokeWeekreport(Integer managerUserId, Integer taskId, Integer userId)
    {
        if (userId == -1)
        {
            userId = managerUserId;
        }
        else
        {
            Integer identityId = userMapper.selectById(managerUserId).getIdentityId();
            if (identityId < IdentityConst.TEAM_LEADER)
            {
                return ResponseResult.errorResult(HttpCodeEnum.NO_AUTH);
            }
        }
        lambdaUpdate().eq(Weekreport::getUserId, userId)
                .eq(Weekreport::getTaskId, taskId)
                .update(new Weekreport(userId, taskId, StatusConst.WEEKREPORT_REVOKE));
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    /***
     * 导出个人周报
     * @param userId
     * @param taskId
     * @return
     * @throws IOException
     */
    public ResponseResult exportUser(Integer userId, Integer taskId) throws IOException {
        List<ExportModelUserVO> exportList = weekreportMapper.getExportModelUserList(userId, taskId);
        if (exportList != null)
        {
            for (ExportModelUserVO vo : exportList)
            {
                Date date = DateFormatUtil.transformStringFormatToDate(vo.getDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int weekDay = calendar.get(Calendar.DAY_OF_WEEK);  // Java的周日是一周的第一天（1），周一为2，以此类推
                System.out.print(weekDay);
                switch (weekDay)
                {
                    case 1:
                        vo.setWeek(DateConst.SUNDAY);
                        break;
                    case 2:
                        vo.setWeek(DateConst.MONDAY);
                        break;
                    case 3:
                        vo.setWeek(DateConst.TUESDAY);
                        break;
                    case 4:
                        vo.setWeek(DateConst.WEDNESDAY);
                        break;
                    case 5:
                        vo.setWeek(DateConst.THURSDAY);
                        break;
                    case 6:
                        vo.setWeek(DateConst.FRIDAY);
                        break;
                    case 7:
                        vo.setWeek(DateConst.SATURDAY);
                        break;
                }
            }
            ExportParams exportParams = new ExportParams(null, "周报");
            Workbook sheets = ExcelExportUtil.exportExcel(exportParams, ExportModelUserVO.class, exportList);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            sheets.write(outputStream);
            outputStream.flush();
            byte[] content = outputStream.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            String url = minioService.uploadExcelFile(String.valueOf(userId) + "/report", "周报_User" + userId + ".xlsx", is);
            return ResponseResult.okResult(url);
        }
        return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
    }

    public ResponseResult addWeekreport(Weekreport weekreport)
    {
        return ResponseResult.okResult(save(weekreport));
    }

    public ResponseResult createNewWeekreportForTask(List<Weekreport> list)
    {
        if (list.size() > 0)
        {
            saveBatch(list);
            return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
    }

    public ResponseResult getSpecifiedReports(SpecifiedReportsDTO dto)
    {
        // 1. 参数校验
        if (dto == null || dto.getUserId() == null || dto.getTaskId() == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }
        if (dto.getTaskId().length == 0)
        {
            dto.setTaskId(taskMapper.getAllTaskIdList().toArray(new Integer[0]));
        }
        if (dto.getUserId().length == 0)
        {
            dto.setUserId(userMapper.getActiveUserIdList().toArray(new Integer[0]));
        }

        // 2. 查询数据
        List<MyWeekreportVO> result = new ArrayList<>();
        Map<Integer, Map<String, List<Weekreport>>> dateTaskMap = new LinkedHashMap<>();
        for (Integer userId : dto.getUserId())
        {
            for (Integer taskId : dto.getTaskId())
            {
                List<String> dateList = weekreportMapper.getDateListByUserIdAndTaskId(userId, taskId);
                for (String date : dateList)
                {
                    List<Weekreport> weekreportList = weekreportMapper.getWeekreportListByUserIdAndTaskIdAndDate(userId, taskId, date);
                    if (!dateTaskMap.containsKey(taskId))
                    {
                        dateTaskMap.put(taskId, new LinkedHashMap<>());
                    }
                    if (!dateTaskMap.get(taskId).containsKey(date))
                    {
                        dateTaskMap.get(taskId).put(date, weekreportList);
                    }
                }
            }
        }

        for (Integer userId : dto.getUserId())
        {
            for (Integer taskId : dto.getTaskId())
            {
                MyWeekreportVO myWeekreportVo = new MyWeekreportVO();
                myWeekreportVo.setUserId(userId);
                myWeekreportVo.setTaskId(taskId);
                Task task = taskMapper.selectById(taskId);
                myWeekreportVo.setStartDate(task.getStartDate());
                myWeekreportVo.setEndDate(task.getEndDate());

                List<WeekreportsVO> rows = new ArrayList<>();
                Map<String, List<Weekreport>> dateTaskReportsMap = dateTaskMap.get(taskId);
                for (String date : dateTaskReportsMap.keySet())
                {
                    List<Weekreport> weekreportList = dateTaskReportsMap.get(date);
                    WeekreportsVO weekreportsVo = new WeekreportsVO();
                    weekreportsVo.setCurDate(date);
                    weekreportsVo.setContent(weekreportList);
                    if (!weekreportList.isEmpty())
                    {
                        myWeekreportVo.setStatus(weekreportList.get(0).getStatus());
                        rows.add(weekreportsVo);
                    }
                }
                myWeekreportVo.setRows(rows);
                result.add(myWeekreportVo);
            }
        }
        return ResponseResult.okResult(result);
    }
}
