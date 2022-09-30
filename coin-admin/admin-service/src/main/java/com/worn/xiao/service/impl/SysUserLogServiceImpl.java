package com.worn.xiao.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.SysUserLogMapper;
import com.worn.xiao.domain.SysUserLog;
import com.worn.xiao.service.SysUserLogService;
@Service
public class SysUserLogServiceImpl extends ServiceImpl<SysUserLogMapper, SysUserLog> implements SysUserLogService{

}
