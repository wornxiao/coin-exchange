package com.worn.xiao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.domain.CashRecharge;
import com.baomidou.mybatisplus.extension.service.IService;
public interface CashRechargeService extends IService<CashRecharge>{


    /**
     * 条件分页查询
     * @param page
     *  分页参数
     * @param coinId
     * 币种的ID
     * @param userId
     * 用户的Id
     * @param userName
     * 用户的名称
     * @param mobile
     * 用户的手机号
     * @param status
     * 审核的状态
     * @param numMin
     * 充值数量的最小值
     * @param numMax
     * 充值数量的最大值
     * @param startTime
     * 充值的开始时间
     * @param endTime
     * 充值数量的结束时间
     * @return
     */
    Page<CashRecharge> findByPage(Page<CashRecharge> page, Long coinId, Long userId, String userName, String mobile,
                                  Byte status, String numMin, String numMax, String startTime, String endTime);
}
