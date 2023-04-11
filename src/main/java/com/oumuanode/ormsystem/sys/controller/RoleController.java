package com.oumuanode.ormsystem.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oumuanode.ormsystem.sys.domin.ResponseResult;
import com.oumuanode.ormsystem.sys.entity.Role;
import com.oumuanode.ormsystem.sys.service.IRoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Api(tags = {"角色控制列表"})
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("/list")
    public ResponseResult<Map<String,Object>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                          @RequestParam(value = "pageNo") Long pageNo,
                                                          @RequestParam(value = "pageSize") Long pageSize){

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);
        wrapper.orderByDesc(Role::getRoleId);

        Page<Role> page = new Page<>(pageNo,pageSize);
        roleService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return ResponseResult.success(data);

    }

    @PostMapping
    public ResponseResult<?> addRole(@RequestBody Role role){
        roleService.addRole(role);
        return ResponseResult.success("新增角色成功");
    }

    @PutMapping
    public ResponseResult<?> updateRole(@RequestBody Role role){
        roleService.updateRole(role);
        return ResponseResult.success("修改角色成功");
    }

    @GetMapping("/{id}")
    public ResponseResult<Role> getRoleById(@PathVariable("id") Integer id){
        Role role = roleService.getRoleById(id);
        return ResponseResult.success(role);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Role> deleteRoleById(@PathVariable("id") Integer id){
        roleService.deleteRoleById(id);
        return ResponseResult.success("删除角色成功");
    }

    @GetMapping("/all")
    public ResponseResult<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return ResponseResult.success(roleList);
    }

}
