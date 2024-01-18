package com.volans.controller;

import com.volans.domain.common.PageResponseResult;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.user.dto.LoginDTO;
import com.volans.domain.user.dto.QueryDTO;
import com.volans.domain.user.pojo.User;
import com.volans.mapper.UserMapper;
import com.volans.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param loginDto 登录数据
     */
    @PostMapping(value = "/login")
    public ResponseResult login(@RequestBody LoginDTO loginDto)
    {
        return userService.login(loginDto);
    }

    /**
     * 添加用户
     * @param user 用户信息
     */
    @PostMapping(value = "/addUser")
    public ResponseResult addUser(@RequestBody User user)
    {
        return ResponseResult.okResult(userService.save(user));
    }

    /**
     * 获取用户列表，没有参数时为全查询，有参数时为条件查询
     * @param queryDto 查询条件
     * @return 用户列表
     */
    @GetMapping(value = "/getUserList")
    public PageResponseResult getUserList(QueryDTO queryDto)
    {
        return userService.getUserList(queryDto);
    }

    /**
     * 更新用户信息
     * @param user
     */
    @PostMapping(value = "/update")
    public ResponseResult update(@RequestBody User user)
    {
        return userService.update(user);
    }

    @GetMapping(value = "/getAllUserIdList")
    public List<Integer> getAllUserIdList()
    {
        return userMapper.getAllUserIdList();
    }

    @GetMapping(value = "/getActiveUserIdList")
    public List<Integer> getActiveUserIdList()
    {
        return userMapper.getActiveUserIdList();
    }

    @PostMapping(value = "/img")
    public ResponseResult uploadUserImg(@RequestHeader("userId") Integer userId, MultipartFile file) throws IOException
    {
        return userService.uploadUserImg(userId, file);
    }

    @GetMapping(value = "/refreshUserInfo")
    public ResponseResult getUser(@RequestHeader("userId") Integer userId)
    {
        return userService.getUserInfo(userId);
    }

    @GetMapping(value = "/getAllUserList")
    public ResponseResult getAllUserList()
    {
        return userService.getAllUserList();
    }
}
