package com.worn.xiao.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.domain.Notice;
import com.worn.xiao.mapper.NoticeMapper;
import com.worn.xiao.service.NoticeService;
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{

}
