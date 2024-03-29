package com.worn.xiao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worn.xiao.domain.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 通过用户的id 查询用户的菜单数据
     * @param userId
     * @return
     */
    List<SysMenu> selectMenusByUserId(Long userId);
}