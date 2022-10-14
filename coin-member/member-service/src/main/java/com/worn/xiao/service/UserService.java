package com.worn.xiao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.worn.xiao.domain.RegisterParam;
import com.worn.xiao.domain.UpdatePhoneParams;
import com.worn.xiao.domain.User;
import com.worn.xiao.domain.UserAuthForm;
import com.worn.xiao.dto.UserDto;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 条件分页查询会员的列表
     * @param page
     * 分页参数
     * @param mobile
     * 会员的手机号
     * @param userId
     * 会员的ID
     * @param userName
     * 会员的名称
     * @param realName
     * 会员的真实名称
     * @param status
     * 会员的状态
     * @param reviewStatus
     * 会员的审核状态
     * @return
     */
    Page<User> findByPage(Page<User> page, String mobile, Long userId,
                          String userName, String realName,
                          Integer status ,
                          Integer reviewStatus);

    /**
     * 通过用户的Id 查询该用户邀请的人员
     * @param page
     * 分页参数
     * @param userId
     * 用户的Id
     * @return
     */
    Page<User> findDirectInvitePage(Page<User> page, Long userId);

    /**
     * 修改用户的审核状态
     * @param id
     * @param authStatus
     * @param authCode
     * @param remark 拒绝的原因
     *
     *
     */
    void updateUserAuthStatus(Long id, Byte authStatus, Long authCode,String remark);

    /**
     * 用户的实名认证
     * @param id 用户的Id
     * @param userAuthForm
     * 认证的表单数据
     * @return
     *  认证的结果
     */
    boolean identifyVerify(Long id, UserAuthForm userAuthForm);

    /**
     * 用户进行高级认证
     * @param valueOf
     * @param asList
     */
    void authUser(Long valueOf, List<String> asList);

    /**
     * 修改用户的手机号号
     * @param userId
     * @param updatePhoneParam
     * @return
     */
    public boolean updatePhone(Long userId, UpdatePhoneParams updatePhoneParam);

    /**
     * 检验新的手机号是否可用,若可用,则给新的手机号发送一个验证码
     * @param mobile
     * 新的手机号
     * @param countryCode
     * 国家代码
     * @return
     */
    boolean checkNewPhone(String mobile, String countryCode);

    /**
     * 用户的注册
     * @param registerParam
     * 注册的表单参数
     * @return
     */
    boolean register(RegisterParam registerParam);


    List<UserDto> getBasicUsers(List<Long> ids);
}
