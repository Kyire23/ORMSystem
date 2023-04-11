package com.oumuanode.ormsystem.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oumuanode.ormsystem.sys.domin.ResponseResult;
import com.oumuanode.ormsystem.sys.entity.User;
import com.oumuanode.ormsystem.sys.service.IUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"用户接口列表"})
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public ResponseResult<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return ResponseResult.success("查询成功");
    }

    @GetMapping("/list")
    public ResponseResult<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                                  @RequestParam(value = "phone",required = false) String phone,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username),User::getUsername,username);
        wrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone);
        wrapper.orderByDesc(User::getId);

        Page<User> page = new Page<>(pageNo,pageSize);
        userService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return ResponseResult.success(data);

    }


    @PostMapping
    public ResponseResult<?> addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return ResponseResult.success("新增用户成功");
    }

    @PutMapping
    public ResponseResult<?> updateUser(@RequestBody User user) {
        user.setPassword(null);
        userService.updateUser(user);
        return ResponseResult.success("修改用户成功");
    }

    @GetMapping("/{id}")
    public ResponseResult<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        return ResponseResult.okResult(user);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<User> deleteUserById(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return ResponseResult.success("删除用户成功");
    }

}
