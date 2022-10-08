package com.worn.xiao.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.UserAuthInfoMapper;
import com.worn.xiao.domain.UserAuthInfo;
import com.worn.xiao.service.UserAuthInfoService;
@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements UserAuthInfoService{

}
