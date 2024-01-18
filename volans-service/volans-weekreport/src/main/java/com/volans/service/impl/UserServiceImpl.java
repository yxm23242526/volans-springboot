package com.volans.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volans.domain.common.PageResponseResult;
import com.volans.domain.common.ResponseResult;
import com.volans.domain.common.enums.HttpCodeEnum;
import com.volans.domain.consts.StatusConst;
import com.volans.domain.user.dto.LoginDTO;
import com.volans.domain.user.dto.QueryDTO;
import com.volans.domain.user.pojo.User;
import com.volans.mapper.UserMapper;
import com.volans.service.MinIOService;
import com.volans.service.UserService;
import com.volans.utils.AppJwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService
{

    @Autowired
    private MinIOService minioService;

    @Override
    public ResponseResult login(LoginDTO loginDto)
    {
        // 1.校验参数
        if (loginDto == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }
        if (ObjectUtils.isEmpty(loginDto.getUserId()))
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }

        // 2.登录验证
        Map<String, Object> result = new HashMap<>();
        // 先根据userId查询用户
        User user = lambdaQuery()
                .eq(User::getUserId, loginDto.getUserId())
                .ne(User::getStatus, StatusConst.USER_DESIGNATED)
                .one();
        if (user == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
        }
        // 再校验用户的密码
        // 客户端提交的是明文密码,要进行密码校验：用户提交的明文密码md5加密， 拿这个加密结果和数据库里的password对比
        String password = loginDto.getPassword();
        String md5Pwd = DigestUtils.md5Hex(password);
        if (!md5Pwd.equals(user.getPassword()))
        {
            return ResponseResult.errorResult(HttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //3. 生成token，组装返回值
        String token = AppJwtUtil.getToken((long) user.getUserId());
        result.put("token", token);
        user.setPassword(null);
        if (user.getImage() == null)
        {
            user.setImage(minioService.getDefaultImgPath());
        }
        result.put("user", user);
        return ResponseResult.okResult(result);
    }

    @Override
    public PageResponseResult getUserList(QueryDTO queryDto)
    {
        if (queryDto.getPage() == null)
        {
            queryDto.setPage(1);
        }
        if (queryDto.getPageSize() == null)
        {
            queryDto.setPageSize(list().size());
        }
        Page<User> list = lambdaQuery()
                .eq(queryDto.getUserId() != null, User::getUserId, queryDto.getUserId())
                .eq(queryDto.getDepartmentId() != null, User::getDepartmentId, queryDto.getDepartmentId())
                .eq(queryDto.getGroupId() != null, User::getGroupId, queryDto.getGroupId())
                .eq(queryDto.getIdentityId() != null, User::getIdentityId, queryDto.getIdentityId())
                .eq(queryDto.getStatus() != null, User::getStatus, queryDto.getStatus())
                .page(new Page<>(queryDto.getPage(), queryDto.getPageSize()));
        List<User> records = list.getRecords();
        for (User user : records)
        {
            user.setPassword(null);
        }
        PageResponseResult result = new PageResponseResult(queryDto.getPage(), queryDto.getPageSize(), (int) list.getTotal());
        result.setData(records);
        return result;
    }

    @Override
    public ResponseResult update(User user)
    {
        if (user == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }
        if (ObjectUtils.isEmpty(user.getUserId()))
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
        }
        if (!lambdaQuery().eq(User::getUserId, user.getUserId()).exists())
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
        }
        updateById(user);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult addUser(User user)
    {
        // 1.参数校验
        if (user == null || user.getUserId() == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }
        // 2.当前用户是否已存在
        if (getById(user.getUserId()) != null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_EXIST);
        }
        // 3.将当前用户存入数据库
        // 3.1 加密密码，初始密码为工号
        user.setPassword(DigestUtils.md5Hex(String.valueOf(user.getUserId())));
        user.setStatus(StatusConst.USER_ONJOB);
        save(user);
        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult uploadUserImg(Integer userId, MultipartFile file) throws IOException
    {
        //1. 校验参数
        if (file == null || file.getSize() <= 0)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID);
        }
        User user = getById(userId);
        if (user == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
        }

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String todayStr = sdf.format(new Date());
        filename = todayStr + suffix;
        String url = minioService.uploadImgFile(String.valueOf(userId) + "/image", filename, file.getInputStream());
        System.out.println(url);
        user.setImage(url);
        update(user);

        return ResponseResult.okResult(HttpCodeEnum.SUCCESS);
    }

    public ResponseResult getUserInfo(Integer userId)
    {
        if (userId == null)
        {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_REQUIRE);
        }
        User user = getById(userId);
        user.setPassword(null);
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        return ResponseResult.okResult(result);
    }

    public ResponseResult getAllUserList()
    {
        List<User> userList = list();
        userList.forEach(user -> user.setPassword(null));
        return ResponseResult.okResult(userList);
    }
}
