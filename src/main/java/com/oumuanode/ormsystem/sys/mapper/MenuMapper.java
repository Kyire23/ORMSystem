package com.oumuanode.ormsystem.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oumuanode.ormsystem.sys.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;



@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    public List<Menu> getMunuListByUserId(@Param("userId") Integer userId, @Param("pid") Integer pid);
}
