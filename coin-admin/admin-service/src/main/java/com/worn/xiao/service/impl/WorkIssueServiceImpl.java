package com.worn.xiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.dto.UserDto;
import com.worn.xiao.feign.UserServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.domain.WorkIssue;
import com.worn.xiao.mapper.WorkIssueMapper;
import com.worn.xiao.service.WorkIssueService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService{

    @Autowired
    private UserServiceFeign userServiceFeign ;
    /**
     * 条件分页查询工单列表
     *
     * @param page      分页参数
     * @param status    工单的状态
     * @param startTime 查询的工单创建起始时间
     * @param endTime   查询的工单创建截至时间
     * @return
     */
    @Override
    public Page<WorkIssue> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime) {
        Page<WorkIssue> pageData = page(page, new LambdaQueryWrapper<WorkIssue>()
                .eq(status != null, WorkIssue::getStatus, status)
                .between(
                        !StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime),
                        WorkIssue::getCreated,
                        startTime, endTime + " 23:59:59")
        );
        if(CollectionUtils.isEmpty(pageData.getRecords())){
            return pageData ;
        }
        List<Long> userIds = pageData.getRecords()
                .stream()
                .map(WorkIssue::getUserId)
                .collect(Collectors.toList());

        Map<Long, UserDto> idMappings = userServiceFeign.getBasicUsers(userIds,
                null,
                null);

        pageData.getRecords().forEach(workIssue->{
            UserDto userDto = idMappings.get(workIssue.getUserId());
            workIssue.setUsername(userDto==null?"测试用户":userDto.getUsername());
            workIssue.setRealName(userDto==null?"测试用户":userDto.getRealName());
        });

        return pageData;
    }



}
