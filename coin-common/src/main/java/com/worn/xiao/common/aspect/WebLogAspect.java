package com.worn.xiao.common.aspect;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.worn.xiao.common.domain.WebLog;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

@Aspect
@Component
@Order(1)
@Slf4j
public class WebLogAspect {


    /**
     * 雪花算法
     * 1 : 机器的id
     * 2 : 应用的id
     */
    private Snowflake snowflake = new Snowflake(1,1) ;

    /**
     * 日志记录：
     *  环绕通知：方法执行之前、之后
     */

    @Pointcut("execution( * com.worn.xiao.common.controller.*.*(..))")
    public void webLog() {
    }

    @Around(value = "webLog()")
    public Object recordWebLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        StopWatch stopWatch = new StopWatch(); // 创建计时器
        stopWatch.start(); //  开始计时器
        result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs()); // 不需要我们自己处理这个异常
        stopWatch.stop(); // 记时结束

        // 获取请求的上下文
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取登录的用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 获取方法
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 获取方法上的ApiOperation注解
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        // 获取目标对象的类型名称
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        // 获取请求的url 地址
        String requestUrl = request.getRequestURL().toString();
        WebLog webLog = WebLog.builder()
                .basePath(StrUtil.removeSuffix(requestUrl, URLUtil.url(requestUrl).getPath()))
                .description(annotation == null ? "no desc" : annotation.value())
                .ip(request.getRemoteAddr())
                .parameter(getMethodParameter(method, proceedingJoinPoint.getArgs()))
                .method(className + "." + method.getName())
                .result(JSONObject.toJSONString(request.getParameterMap()))
                .recodeTime(System.currentTimeMillis())
                .spendTime(stopWatch.getTime())
                .uri(request.getRequestURI())
                .url(request.getRequestURL().toString())
                .username(authentication == null ? "anonymous" : authentication.getPrincipal().toString())
                .build();
        log.info(JSON.toJSONString(webLog, true));
        return result;
    }

    /**
     * {
     * "":value,
     * "":"value"
     * }
     *
     * @param method
     * @param args
     * @return
     */
    private Object getMethodParameter(Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
        HashMap<String, Object> methodParameters = new HashMap<>();
        if (args != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                methodParameters.put(parameterNames[i], args[i] == null ? "" : JSON.toJSONString(args[i]));
            }
        }
        return methodParameters;
    }
}