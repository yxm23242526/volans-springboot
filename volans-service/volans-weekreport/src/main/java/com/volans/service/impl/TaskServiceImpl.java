package com.volans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.common.enums.HttpCodeEnum;
import com.volans.domain.consts.StatusConst;
import com.volans.domain.task.pojo.Task;
import com.volans.domain.task.vo.TaskInfoVO;
import com.volans.domain.weekreport.pojo.Weekreport;
import com.volans.mapper.TaskMapper;
import com.volans.mapper.UserMapper;
import com.volans.service.TaskService;
import com.volans.service.WeekreportService;
import com.volans.utils.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService
{
    @Autowired
    private UserMapper UserMapper;

    @Autowired
    private WeekreportService weekreportService;

    @Autowired
    private TaskMapper taskMapper;

    @Scheduled(cron="0 0 4 * * FRI") // 每周五凌晨4点
    public void addTask()
    {
        System.out.println(new Date() + ":定时任务开始");

        List<String> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.FRIDAY);  // 设置一周的开始天为周五
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_WEEK, 7);  // 将日期设置为下周四，并添加一周
        Date endDate = calendar.getTime();  // 获取下周四的日期

        while (startDate.before(endDate)) {
            dateList.add(DateFormatUtil.transformDateFormatToString(startDate, "yyyy-MM-dd"));
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_WEEK, 1);  // 逐日向后移动日期
            startDate = calendar.getTime();
        }

        List<Task> list = list();
        for(Task task : list)
        {
            if (task.getStartDate() == dateList.get(0) || task.getEndDate() == dateList.get(dateList.size() - 1))
            {
                return;
            }
        }

        Task task = new Task();
        task.setStartDate(dateList.get(0));
        task.setEndDate(dateList.get(dateList.size() - 1));
        save(task);

        List<Integer> userIds = UserMapper.getActiveUserIdList();

        List<Weekreport> weekreportList = new ArrayList<>();
        for(int id : userIds)
        {
            for(String date : dateList)
            {
                Weekreport newWeekreport = new Weekreport();
                newWeekreport.setTaskId(task.getTaskId());
                newWeekreport.setDate(date);
                newWeekreport.setUserId(id);
                newWeekreport.setStatus(StatusConst.WEEKREPORT_NEWTASK);
                weekreportList.add(newWeekreport);
            }
        }
        weekreportService.createNewWeekreportForTask(weekreportList);
    }

    public ResponseResult getTaskListInfo()
    {
        List<TaskInfoVO> TaskInfo = taskMapper.selectTaskInfo();
        return ResponseResult.okResult(TaskInfo);
    }

    public ResponseResult addTask(Task task)
    {
        Date startDate = DateFormatUtil.transformStringFormatToDate(task.getStartDate());
        Date endDate = DateFormatUtil.transformStringFormatToDate(task.getEndDate());  // 获取下周四的日期

        List<String> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        while (startDate.before(endDate)) {
            dateList.add(DateFormatUtil.transformDateFormatToString(startDate, "yyyy-MM-dd"));
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_WEEK, 1);  // 逐日向后移动日期
            startDate = calendar.getTime();
        }
        dateList.add(task.getEndDate());

        List<Task> list = list();
        for(Task task2 : list)
        {
            if (task2.getStartDate() == dateList.get(0) || task2.getEndDate() == dateList.get(dateList.size() - 1))
            {
                return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID);
            }
        }

        save(task);

        List<Integer> userIds = UserMapper.getActiveUserIdList();

        List<Weekreport> weekreportList = new ArrayList<>();
        for(int id : userIds)
        {
            for(String date : dateList)
            {
                Weekreport newWeekreport = new Weekreport();
                newWeekreport.setTaskId(task.getTaskId());
                newWeekreport.setDate(date);
                newWeekreport.setUserId(id);
                newWeekreport.setStatus(StatusConst.WEEKREPORT_NEWTASK);
                weekreportList.add(newWeekreport);
            }
        }
        weekreportService.createNewWeekreportForTask(weekreportList);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    public ResponseResult editTask(Task task)
    {
        // 1. 校验参数
        if (task == null || task.getTaskId() == null || task.getStartDate() == null || task.getEndDate() == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID);
        }
        if (!lambdaQuery().eq(Task::getTaskId, task.getTaskId()).exists())
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
        }

        // 2. 数据更新
        updateById(task);

        // 3. 返回结果
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    public ResponseResult deleteTask(Integer taskId)
    {
        // 1. 参数校验
        if (taskId == null || taskId < 0)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID);
        }
        if (!lambdaQuery().eq(Task::getTaskId, taskId).exists())
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
        }

        // 2. 如果当前Task已经有人填写就不能删除
        if (weekreportService.lambdaQuery().eq(Weekreport::getTaskId, taskId).ne(Weekreport::getStatus, StatusConst.WEEKREPORT_NEWTASK).exists())
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_HAS_RELATION);
        }

        // 3. 数据更新
        List<Weekreport> list = weekreportService.lambdaQuery().eq(Weekreport::getTaskId, taskId).list();
        List<Integer> idList = list.stream().map(Weekreport::getWeekreportId).collect(Collectors.toList());
        weekreportService.removeBatchByIds(idList);
        removeById(taskId);

        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }
}
