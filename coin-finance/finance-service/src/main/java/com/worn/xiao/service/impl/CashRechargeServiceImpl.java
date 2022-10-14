package com.worn.xiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.dto.UserDto;
import com.worn.xiao.feign.UserServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.mapper.CashRechargeMapper;
import com.worn.xiao.domain.CashRecharge;
import com.worn.xiao.service.CashRechargeService;
import org.springframework.util.CollectionUtils;

@Service
public class CashRechargeServiceImpl extends ServiceImpl<CashRechargeMapper, CashRecharge> implements CashRechargeService{

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
     * 条件分页查询
     *
     * @param page      分页参数
     * @param coinId    币种的ID
     * @param userId    用户的Id
     * @param userName  用户的名称
     * @param mobile    用户的手机号
     * @param status    审核的状态
     * @param numMin    充值数量的最小值
     * @param numMax    充值数量的最大值
     * @param startTime 充值的开始时间
     * @param endTime   充值数量的结束时间
     * @return
     */
    @Override
    public Page<CashRecharge> findByPage(Page<CashRecharge> page, Long coinId, Long userId, String userName,
                                         String mobile, Byte status, String numMin, String numMax, String startTime,
                                         String endTime) {
        LambdaQueryWrapper<CashRecharge> cashRechargeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 1 使用用户相关的字段进行查询
        Map<Long, UserDto> basicUsers = null;
        if (userId != null || !StringUtils.isEmpty(userName) || !StringUtils.isEmpty(mobile)) {
            basicUsers = userServiceFeign.getBasicUsers(userId == null ? null : Arrays.asList(userId), userName, mobile);
            if (CollectionUtils.isEmpty(basicUsers)) { // 没有用户
                return page;
            }
            cashRechargeLambdaQueryWrapper.in(CashRecharge::getUserId, basicUsers.keySet()); // 使用用户的信息做条件
        }
        // 添加其他的条件
        cashRechargeLambdaQueryWrapper.eq(coinId != null, CashRecharge::getCoinId, coinId)
                .eq(status != null, CashRecharge::getStatus, status)
                .between(
                        !(StringUtils.isEmpty(numMin) || StringUtils.isEmpty(numMax)),
                        CashRecharge::getNum,
                        new BigDecimal(numMin==null? "0" :numMin), new BigDecimal(numMax==null? "0" :numMax)
                )
                .between(
                        !(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)),
                        CashRecharge::getCreated,
                        startTime, endTime + "23:23:59"
                );
        // 查询
        Page<CashRecharge> pageData = page(page, cashRechargeLambdaQueryWrapper);
        // 获取查询的数据
        List<CashRecharge> records = pageData.getRecords();
        if(!CollectionUtils.isEmpty(records)){
            List<Long> userIds=new ArrayList<>();
            if(basicUsers==null){ // 说明前面没有使用用户的信息查询用户
                userIds= records
                        .stream()
                        .map(CashRecharge::getUserId)
                        .collect(Collectors.toList());
            }
            Map<Long, UserDto> finalBasicUsers = userServiceFeign.getBasicUsers(userIds,null,null);
            records.forEach(record->{
                UserDto userDto = finalBasicUsers.get(record.getUserId());
                if(userDto!=null){
                    record.setUsername(userDto.getUsername());
                    record.setRealName(userDto.getRealName());
                }
            });
        }
        return pageData ;
    }
}
