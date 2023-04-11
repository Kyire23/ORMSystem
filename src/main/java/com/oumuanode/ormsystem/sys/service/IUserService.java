package com.oumuanode.ormsystem.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oumuanode.ormsystem.sys.domin.ResponseResult;
import com.oumuanode.ormsystem.sys.entity.User;


import java.util.Map;

public interface IUserService extends IService<User> {


    ResponseResult login(User user);

    ResponseResult logout();

    Map<String, Object> getUserInfo(String token);

    void logout(String token);


    void addUser(User user);

    void updateUser(User user);

    User getUserById(Integer id);

    void deleteUserById(Integer id);
}
