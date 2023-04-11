package com.oumuanode.ormsystem.sys.controller;


import com.oumuanode.ormsystem.enmus.AppHttpCodeEnum;
import com.oumuanode.ormsystem.exeception.SysException;
import com.oumuanode.ormsystem.sys.domin.ResponseResult;
import com.oumuanode.ormsystem.sys.entity.User;
import com.oumuanode.ormsystem.sys.service.IUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Api(tags = {"登录接口列表"})
@RestController
@RequestMapping("/user")
public class AdminLoginController {
    @Autowired
    private IUserService userService;


    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        //字符串 不是 null ，并且不为空，而且不能是空白字符
        if (!StringUtils.hasText(user.getUsername())){
            throw new SysException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return userService.login(user);
    }


    @GetMapping("/info")
    public ResponseResult<Map<String,Object>> getUserInfo(@RequestParam("token")String token){
        Map<String,Object> data = userService.getUserInfo(token);
        if (data == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_INVALID);
        }
        return ResponseResult.okResult(data);
    }


    @PostMapping("/logout")
    public ResponseResult<?> logout(@RequestHeader("X-Token") String token){
        //清除token即可
        userService.logout(token);
        return ResponseResult.success("退出成功");
    }

}