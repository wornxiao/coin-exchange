package com.worn.xiao.model;

import com.worn.xiao.domain.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {

    /**
     * 登录产生的token
     */
    private String token ;

    /**
     * 前端的菜单数据
     */
    private List<SysMenu> menus ;

    /**
     * 权限数据
     */
    private List<SimpleGrantedAuthority> authorities ;

}