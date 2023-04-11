package com.oumuanode.ormsystem.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oumuanode.ormsystem.sys.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;



@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    public List<Integer> getMenuIdListByRoleId(Integer roleId);

}
