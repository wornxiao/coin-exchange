package com.worn.xiao.service;

import com.worn.xiao.domain.RolePrivilegesParam;
import com.worn.xiao.domain.SysMenu;
import com.worn.xiao.domain.SysRolePrivilege;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRolePrivilegeService extends IService<SysRolePrivilege>{

    /**
     * 查询角色的权限
     * @param roleId
     * @return
     */
    List<SysMenu> findSysMenuAndPrivileges(Long roleId);


    /**
     * 给角色授权权限
     * @param rolePrivilegesParam
     * @return
     */
    boolean grantPrivileges(RolePrivilegesParam rolePrivilegesParam);

}
