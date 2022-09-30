package com.worn.xiao.controller;

import com.worn.xiao.common.constant.R;
import com.worn.xiao.domain.SysUser;
import com.worn.xiao.service.SysUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "admin-service-test接口")
public class TestController {

    @Autowired
    private SysUserService sysUserService ;

    @GetMapping("/user/info/{id}")
    @ApiOperation(value = "使用用户的id查询用户",authorizations = {@Authorization("Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户的id值")
    })
    public R<SysUser> sysUser(@PathVariable("id")Long id){
        SysUser sysUser = sysUserService.getById(id);
        return R.ok(sysUser) ;
    }

}