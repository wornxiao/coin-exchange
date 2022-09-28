package com.worn.xiao.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worn.xiao.common.constant.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Api(tags = "CoinCommon里面测试的接口")
public class TestController {

    @Autowired
    private ObjectMapper objectMapper ;

    @GetMapping("/common/test")
    @ApiOperation(value = "测试方法", authorizations = {@Authorization("Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "参数1", dataType = "String", paramType = "query", example = "paramValue"),
            @ApiImplicitParam(name = "param1", value = "参数2", dataType = "String", paramType = "query", example = "paramValue")
    })
    public R<String> testMethod(String param, String param1) throws InterruptedException {
        Thread.sleep(2000);
        return R.ok("ok");
    }

    @GetMapping("/common/date")
    @ApiOperation(value = "日志格式化测试", authorizations = {@Authorization("Authorization")})
    public R<Date> testMethod() {
        return R.ok(new Date());
    }

}