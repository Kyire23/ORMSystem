package com.oumuanode.ormsystem.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oumuanode.ormsystem.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {
    public List<String> getRoleNameByUserId(Integer userId);
}
