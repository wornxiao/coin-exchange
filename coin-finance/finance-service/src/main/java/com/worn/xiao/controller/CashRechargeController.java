package com.worn.xiao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.common.constant.R;
import com.worn.xiao.domain.CashRecharge;
import com.worn.xiao.service.CashRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/cashRecharges")
@Api(tags = "GCN充值控制器")
public class CashRechargeController {


    @Autowired
    private CashRechargeService cashRechargeService;

    @GetMapping("/records")
    @ApiOperation(value = "条件分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
            @ApiImplicitParam(name = "coinId", value = "币种的Id"),
            @ApiImplicitParam(name = "userId", value = "用户的Id"),
            @ApiImplicitParam(name = "userName", value = "用户的名称"),
            @ApiImplicitParam(name = "mobile", value = "用户的手机号"),
            @ApiImplicitParam(name = "status", value = "充值审核状态"),
            @ApiImplicitParam(name = "numMin", value = "充值最小金额"),
            @ApiImplicitParam(name = "numMax", value = "充值最大金额"),
            @ApiImplicitParam(name = "startTime", value = "充值开始时间"),
            @ApiImplicitParam(name = "endTime", value = "充值结束时间"),

    })
    public R<Page<CashRecharge>> findByPage(
            @ApiIgnore Page<CashRecharge> page,
            Long coinId, Long userId, String userName,
            String mobile, Byte status, String numMin, String numMax,
            String startTime, String endTime
    ) {
        Page<CashRecharge> pageData = cashRechargeService.findByPage(page, coinId, userId, userName, mobile, status, numMin, numMax, startTime, endTime);
        return R.ok(pageData);
    }
}