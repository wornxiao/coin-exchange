package com.worn.xiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.UserBankMapper;
import com.worn.xiao.domain.UserBank;
import com.worn.xiao.service.UserBankService;
@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService{
    /**
     * 查询用户的银行卡信息
     *
     * @param page  分页参数
     * @param usrId 用户的Id
     * @return
     */
    @Override
    public Page<UserBank> findByPage(Page<UserBank> page, Long usrId) {
        return page(page ,new LambdaQueryWrapper<UserBank>().eq(usrId != null ,UserBank::getUserId ,usrId));
    }
}
