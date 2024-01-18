package com.volans.domain.user.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    // 工号
    @TableId
    private Integer userId;

    // 姓名
    private String userName;

    // 昵称
    private String nickName;

    // 密码
    private String password;

    // 头像
    private String image;

    // 个性签名
    private String signature;

    // 部门ID
    private Integer departmentId;

    // 组ID
    private Integer groupId;

    // 手机号
    private String phone;

    // 身份
    private Integer identityId;

    // 状态
    private Integer status;
}
