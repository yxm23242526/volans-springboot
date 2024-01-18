package com.volans.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.volans.domain.GatewayLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<GatewayLog>
{
}
