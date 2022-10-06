package com.worn.xiao.domain;

import lombok.Data;

import java.util.List;

@Data
public class RolePrivilegesParam extends SysRolePrivilege {
    private List<Long> privilegeIds;
}
