package com.volans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.volans.domain.weekreport.dto.QueryDTO;
import com.volans.domain.weekreport.pojo.Weekreport;
import com.volans.domain.weekreport.vo.ExportModelOneVO;
import com.volans.domain.weekreport.vo.ExportModelTwoVO;
import com.volans.domain.weekreport.vo.ExportModelUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WeekreportMapper extends BaseMapper<Weekreport>
{
    @Select("SELECT DISTINCT task_id FROM weekreport WHERE user_id = #{userId} ORDER BY task_id DESC")
    public List<Integer> getTaskList(@Param("userId")Integer userId);

    @Select("SELECT * FROM weekreport WHERE user_id = #{userId} AND task_id = #{taskId}")
    public List<Weekreport> getWeekreportListByUserIdAndTaskId(@Param("userId")Integer userId, @Param("taskId")Integer taskId);

    @Select("SELECT DISTINCT date FROM weekreport WHERE user_id = #{userId} AND task_id = #{taskId} ORDER BY date")
    public List<String> getDateListByUserIdAndTaskId(@Param("userId") Integer userId, @Param("taskId")Integer taskId);

    @Select("SELECT * FROM weekreport WHERE user_id = #{userId} AND task_id = #{taskId} AND date = #{date}")
    public List<Weekreport>  getWeekreportListByUserIdAndTaskIdAndDate(@Param("userId")Integer userId, @Param("taskId")Integer taskId, @Param("date")String date);

    public List<ExportModelOneVO> getExportModelOneList(QueryDTO queryDto);

    public List<ExportModelTwoVO> getExportModelTwoList(QueryDTO queryDto);

    @Select("SELECT weekreport_id FROM weekreport WHERE user_id = #{userId} AND task_id = #{taskId}")
    public List<Integer> getWeekreportIdListByUserIdAndTaskId(@Param("userId")Integer userId, @Param("taskId")Integer taskId);

    public List<ExportModelUserVO> getExportModelUserList(@Param("userId")Integer userId, @Param("taskId")Integer taskId);
}
