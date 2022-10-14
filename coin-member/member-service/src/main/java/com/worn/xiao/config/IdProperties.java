package com.worn.xiao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "identify")
@Data
public class IdProperties {

    /**
     * 对应你购买的appKey
     */
    private String appKey ;

    /**
     * 对应你购买的appSecret
     */
    private String appSecret ;


    /**
     * 对应你购买的appCode
     */
    private String appCode ;

    /**
     * 认证的url地址
     */
    private String url ;
}
