package com.oumuanode.ormsystem.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oumuanode.ormsystem.sys.entity.Role;


public interface IRoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer id);

    void updateRole(Role role);

    void deleteRoleById(Integer id);
}
