package com.worn.xiao.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.common.constant.R;
import com.worn.xiao.domain.SysUser;
import com.worn.xiao.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

@RestController
public class SysUserController {

    @Autowired
    private SysUserService sysUserService ;

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current" ,value = "当前页") ,
            @ApiImplicitParam(name = "size" ,value = "每页显示的条数") ,
            @ApiImplicitParam(name = "mobile" ,value = "员工的手机号码") ,
            @ApiImplicitParam(name = "fullname" ,value = "员工的全名称") ,
    })
    @PreAuthorize("hasAuthority('sys_user_query')")
    public R<Page<SysUser>> findByPage(@ApiIgnore Page<SysUser> page , String mobile , String fullname){
        page.addOrder(OrderItem.desc("last_update_time")) ;
        Page<SysUser> pageData =  sysUserService.findByPage(page,mobile ,fullname) ;
        return R.ok(pageData) ;
    }


    @PostMapping
    @ApiOperation(value = "新增员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser"  ,value = "sysUser 的json数据")
    })
    @PreAuthorize("hasAuthority('sys_user_create')")
    public R addUser(@RequestBody SysUser sysUser){
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        sysUser.setCreateBy(userId);
        boolean isOk = sysUserService.addUser(sysUser) ;
        if(isOk){
            return R.ok() ;
        }
        return R.fail("新增失败") ;
    }



    @PostMapping("/delete")
    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAuthority('sys_user_delete')")
    public R deleteUser( @RequestBody  Long ids[] ){
        boolean b = sysUserService.removeByIds(Arrays.asList(ids));
        if(b){
            return R.ok() ;
        }
        return R.fail() ;
    }
}