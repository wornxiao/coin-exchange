package com.worn.xiao.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.WebConfigMapper;
import com.worn.xiao.domain.WebConfig;
import com.worn.xiao.service.WebConfigService;
@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService{

}
