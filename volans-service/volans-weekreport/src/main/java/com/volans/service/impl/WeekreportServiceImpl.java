package com.volans.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.common.enums.HttpCodeEnum;
import com.volans.domain.consts.DateConst;
import com.volans.domain.consts.StatusConst;
import com.volans.domain.task.pojo.Task;
import com.volans.domain.weekreport.dto.QueryDTO;
import com.volans.domain.weekreport.pojo.Weekreport;
import com.volans.domain.weekreport.vo.*;
import com.volans.mapper.TaskMapper;
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
            for (ExportModelOneVO exportVO : exportList)
            {
                if (exportVO != null)
                {
                    exportVO.setNumber(count++);
                }
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
                String url = minioService.uploadExcelFile(String.valueOf(userId), queryDto.getStartDate() == null && queryDto.getEndDate() == null ? "周报.xlsx" : queryDto.getStartDate() + "_" + queryDto.getEndDate() + ".xlsx", is);
                return ResponseResult.okResult(url);
            }
            return ResponseResult.okResult(exportList);
        }
        else
        {
            List<ExportModelTwoVO> exportList = weekreportMapper.getExportModelTwoList(queryDto);
            int count = 1;
            for (ExportModelTwoVO exportVO : exportList)
            {
                if (exportVO != null)
                {
                    exportVO.setNumber(count++);
                }
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
                String url = minioService.uploadExcelFile(String.valueOf(userId) + "/report", queryDto.getStartDate() == null && queryDto.getEndDate() == null ? "周报.xlsx" : queryDto.getStartDate() + "_" + queryDto.getEndDate() + ".xlsx", is);
                return ResponseResult.okResult(url);
            }
            return ResponseResult.okResult(exportList);
        }
    }

    @Override
    public ResponseResult submitWeekreport(Integer userId, List<Weekreport> list)
    {
        // 1. 校验参数
        if (list == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID);
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
    public ResponseResult revokeWeekreport(Integer userId, Integer taskId)
    {
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
}
