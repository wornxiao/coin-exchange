package com.worn.xiao.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.domain.SysUser;
import com.worn.xiao.mapper.SysUserMapper;
import com.worn.xiao.service.SysUserService;
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

}
