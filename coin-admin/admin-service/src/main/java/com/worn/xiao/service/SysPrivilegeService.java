package com.worn.xiao.service;

import com.worn.xiao.domain.SysPrivilege;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPrivilegeService extends IService<SysPrivilege>{

    /**
     *  获取该菜单下面所有的权限
     * @param roleId
     *          roleId 代表当前的查询的角色的ID
     * @param menuId  菜单的ID
     *
     * @return
     */
    List<SysPrivilege> getAllSysPrivilege(Long menuId , Long roleId);

}
