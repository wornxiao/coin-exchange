package com.worn.xiao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.domain.UserBank;
import com.baomidou.mybatisplus.extension.service.IService;
public interface UserBankService extends IService<UserBank>{


    /**
     * 查询用户的银行卡信息
     * @param page
     * 分页参数
     * @param usrId
     * 用户的Id
     * @return
     */
    Page<UserBank> findByPage(Page<UserBank> page, Long usrId);
}
