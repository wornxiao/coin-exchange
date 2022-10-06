package com.worn.xiao.service.impl;

import com.worn.xiao.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.SysMenuMapper;
import com.worn.xiao.domain.SysMenu;
import com.worn.xiao.service.SysMenuService;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{

    @Autowired
    private SysRoleService sysRoleService ;

    @Autowired
    private SysMenuMapper sysMenuMapper ;
    /**
     * 通过用户的id 查询用户的菜单数据
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        // 1 如果该用户是超级管理员->>拥有所有的菜单
        if(sysRoleService.isSuperAdmin(userId)){
            return list() ; // 查询所有
        }
        // 2 如果该用户不是超级管理员->>查询角色->查询菜单
        return sysMenuMapper.selectMenusByUserId(userId);
    }
}
