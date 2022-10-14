package com.worn.xiao.config;

import com.worn.xiao.geetest.GeetestLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GeetestProperties.class)
public class GeetestAutoConfiguration {

    @Autowired
    private GeetestProperties geetestProperties ;

    public GeetestAutoConfiguration(GeetestProperties geetestProperties){
        this.geetestProperties = geetestProperties ;
    }

    @Bean
    public GeetestLib geetestLib(){
        GeetestLib geetestLib =
                new GeetestLib(geetestProperties.getGeetestId(),
                geetestProperties.getGeetestKey());
        return geetestLib ;
    }
}