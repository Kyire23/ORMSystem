package com.oumuanode.ormsystem.sys.controller;

import com.oumuanode.ormsystem.sys.domin.ResponseResult;
import com.oumuanode.ormsystem.sys.entity.Menu;
import com.oumuanode.ormsystem.sys.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(tags = {"菜单接口列表"})
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @ApiOperation("查询所有菜单数据")
    @GetMapping
    public ResponseResult<List<Menu>> getAllMenu(){
        List<Menu> menuList = menuService.getAllMenu();
        return ResponseResult.success(menuList);
    }

}