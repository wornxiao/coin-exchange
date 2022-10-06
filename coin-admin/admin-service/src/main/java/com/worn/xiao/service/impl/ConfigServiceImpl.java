package com.worn.xiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.ConfigMapper;
import com.worn.xiao.domain.Config;
import com.worn.xiao.service.ConfigService;
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService{
    /**
     * 条件分页查询参数
     *
     * @param page 分页参数
     * @param type 类型
     * @param name 参数名称
     * @param code 参数Code
     * @return
     */
    @Override
    public Page<Config> findByPage(Page<Config> page, String type, String name, String code) {
        return page(page,new LambdaQueryWrapper<Config>()
                .like(!StringUtils.isEmpty(type),Config::getType ,type)
                .like(!StringUtils.isEmpty(name),Config::getName ,name)
                .like(!StringUtils.isEmpty(code),Config::getCode ,code)
        );
    }
}
