package com.volans.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.volans.domain.common.PageResponseResult;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.user.dto.LoginDTO;
import com.volans.domain.user.dto.QueryDTO;
import com.volans.domain.user.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService extends IService<User>
{
    public ResponseResult login(LoginDTO loginDto);

    public PageResponseResult getUserList(QueryDTO queryDto);

    public ResponseResult updateUser(User user);

    public ResponseResult addUser(User user);

    public ResponseResult uploadUserImg(Integer userId, MultipartFile file) throws IOException;

    public ResponseResult getUserInfo(Integer userId);

    public ResponseResult getAllActiveUserList();

    public ResponseResult deleteUser(Integer userId);
}
