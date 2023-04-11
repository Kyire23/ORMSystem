package com.oumuanode.ormsystem.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oumuanode.ormsystem.sys.domin.ResponseResult;
import com.oumuanode.ormsystem.sys.domin.vo.PageVo;
import com.oumuanode.ormsystem.sys.domin.vo.UserVo;
import com.oumuanode.ormsystem.sys.entity.Menu;
import com.oumuanode.ormsystem.sys.entity.User;
import com.oumuanode.ormsystem.sys.entity.UserRole;
import com.oumuanode.ormsystem.sys.mapper.UserMapper;
import com.oumuanode.ormsystem.sys.mapper.UserRoleMapper;
import com.oumuanode.ormsystem.sys.service.IMenuService;
import com.oumuanode.ormsystem.sys.service.IUserService;
import com.oumuanode.ormsystem.utils.BeanCopyUtils;
import com.oumuanode.ormsystem.utils.JwtUtil;
import com.oumuanode.ormsystem.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRoleMapper userRoleMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IMenuService menuService;
    @Autowired
    private RedisCache redisCache;



    @Override
    public ResponseResult login(User user) {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getUsername,user.getUsername());
        User loginUser = this.baseMapper.selectOne(query);
        if (loginUser != null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())) {
            // 存入redis
            redisCache.setCacheObject("login"+loginUser.getPassword(),loginUser.getUsername());
//            loginUser.setPassword(null);
            // 创建jwt
            String token = jwtUtil.createToken(loginUser);
            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token",token);

            return ResponseResult.success(data);
        }
        return null;

    }

    @Override
    public ResponseResult logout() {
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 根据token获取用户信息，redis
        //Object obj = redisTemplate.opsForValue().get(token);
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token, User.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(loginUser != null){
            //User loginUser = JSON.parseObject(JSON.toJSONString(obj),User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());

            // 角色
            List<String> roleList = this.baseMapper.getRoleNameByUserId((loginUser.getId()));
            data.put("roles",roleList);

            // 权限列表
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList",menuList);

            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {

    }


    @Override
    public void addUser(User user) {
        this.baseMapper.insert(user);
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null){
            for (Integer roleId : roleIdList){
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    @Override
    public void updateUser(User user) {
        this.baseMapper.updateById(user);
        // 清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(wrapper);
        // 设置新的角色
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    @Override
    public User getUserById(Integer id) {
        User user = this.baseMapper.selectById(id);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        List<Integer> collect = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        user.setRoleIdList(collect);
        return user;
    }

    @Override
    public void deleteUserById(Integer id) {
        this.baseMapper.deleteById(id);
        // 清除原有角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        userRoleMapper.delete(wrapper);
    }
}
