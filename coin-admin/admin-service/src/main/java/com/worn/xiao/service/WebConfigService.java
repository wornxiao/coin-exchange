package com.worn.xiao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.domain.WebConfig;
import com.baomidou.mybatisplus.extension.service.IService;
public interface WebConfigService extends IService<WebConfig>{


    Page<WebConfig> findByPage(Page<WebConfig> page, String name, String type);
}
