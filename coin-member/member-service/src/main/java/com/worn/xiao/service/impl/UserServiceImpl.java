package com.worn.xiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.domain.User;
import com.worn.xiao.mapper.UserMapper;
import com.worn.xiao.service.UserService;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    /**
     * 条件分页查询会员的列表
     *
     * @param page     分页参数
     * @param mobile   会员的手机号
     * @param userId   会员的ID
     * @param userName 会员的名称
     * @param realName 会员的真实名称
     * @param status   会员的状态
     * @return
     */
    @Override
    public Page<User> findByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer status) {
        return page(page,
                new LambdaQueryWrapper<User>()
                        .like(!StringUtils.isEmpty(mobile),User::getMobile,mobile)
                        .like(!StringUtils.isEmpty(userName),User::getUsername,userName)
                        .like(!StringUtils.isEmpty(realName),User::getRealName,realName)
                        .eq(userId!=null ,User::getId ,userId)
                        .eq(status!=null ,User::getStatus ,status)
        )   ;
    }

    /**
     * 通过用户的Id 查询该用户邀请的人员
     *
     * @param page   分页参数
     * @param userId 用户的Id
     * @return
     */
    @Override
    public Page<User> findDirectInvitePage(Page<User> page, Long userId) {
        return page(page,new LambdaQueryWrapper<User>().eq(User::getDirectInviteid,userId));
    }
}
