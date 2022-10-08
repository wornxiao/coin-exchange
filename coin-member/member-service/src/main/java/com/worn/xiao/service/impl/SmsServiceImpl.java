package com.worn.xiao.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.SmsMapper;
import com.worn.xiao.domain.Sms;
import com.worn.xiao.service.SmsService;
@Service
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements SmsService{

}
