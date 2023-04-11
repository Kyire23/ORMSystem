package com.oumuanode.ormsystem.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oumuanode.ormsystem.sys.entity.Menu;

import java.util.List;



public interface IMenuService extends IService<Menu> {

    List<Menu> getMenuListByUserId(Integer id);

    List<Menu> getAllMenu();
}
