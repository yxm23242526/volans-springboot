package com.volans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volans.domain.consts.StatusConst;
import com.volans.domain.task.pojo.Task;
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

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService
{
    @Autowired
    private UserMapper UserMapper;

    @Autowired
    private WeekreportService weekreportService;


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
}
