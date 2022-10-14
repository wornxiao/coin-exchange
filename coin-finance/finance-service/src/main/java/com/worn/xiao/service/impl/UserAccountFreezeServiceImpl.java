package com.worn.xiao.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.UserAccountFreezeMapper;
import com.worn.xiao.domain.UserAccountFreeze;
import com.worn.xiao.service.UserAccountFreezeService;
@Service
public class UserAccountFreezeServiceImpl extends ServiceImpl<UserAccountFreezeMapper, UserAccountFreeze> implements UserAccountFreezeService{

}
