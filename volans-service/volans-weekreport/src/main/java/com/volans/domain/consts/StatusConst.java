package com.volans.domain.consts;

public class StatusConst
{
    // 用户状态
    // 用户状态--离职
    public static final Integer USER_DESIGNATED = 0;

    // 用户状态--在职
    public static final Integer USER_ONJOB = 1;

    // 周报状态
    // 周报状态--未提交
    public static final Integer WEEKREPORT_UNCOMMITED = 0;
    // 周报状态--新任务
    public static final Integer WEEKREPORT_NEWTASK = 1;
    // 周报状态--已提交
    public static final Integer WEEKREPORT_COMMITTED = 2;
    // 周报状态--回退
    public static final Integer WEEKREPORT_REVOKE = 3;

}
