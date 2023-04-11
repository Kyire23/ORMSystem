package com.oumuanode.ormsystem.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oumuanode.ormsystem.sys.entity.UserRole;
import com.oumuanode.ormsystem.sys.mapper.UserRoleMapper;
import com.oumuanode.ormsystem.sys.service.IUserRoleService;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
