package com.worn.xiao.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@EnableAuthorizationServer
@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public PasswordEncoder passwordEncoder ;

    @Autowired
    private AuthenticationManager authenticationManager ;

    @Qualifier(value = "userDetailServiceImpl")
    private UserDetailsService userDetailsService ;

    /**
     * 配置第三方客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("coin-api")
                .secret(passwordEncoder.encode("coin-secret"))
                .scopes("all")
                .authorizedGrantTypes("password","refresh")
                .accessTokenValiditySeconds(24 * 7200)
                .refreshTokenValiditySeconds(7 *  24 * 7200)
                .and()
                .withClient("inside-app")
                .secret(passwordEncoder.encode("inside-secret"))
                .secret("all")
                .authorizedGrantTypes("client_credentials")
                .accessTokenValiditySeconds(7 * 24 *3600) ;

        super.configure(clients);
    }

    /**
     * 设置授权管理器和UserDetailsService
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(new InMemoryTokenStore())
                .authenticationManager(authenticationManager)
                .tokenStore(jwtTokenStore()) //设置token 存储在哪里
                .tokenEnhancer(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService) ;
    }

    private TokenEnhancer jwtAccessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter() ;
        // 读取classpath 下面的密钥文件
        ClassPathResource classPathResource = new ClassPathResource("coinexchange.jks");
        // 获取KeyStoreFactory
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,"coinexchange".toCharArray()) ;
        // 给JwtAccessTokenConverter 设置一个密钥对
        tokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("coinexchange","coinexchange".toCharArray()));
        return  tokenConverter ;
    }

    private TokenStore jwtTokenStore() {
        JwtTokenStore jwtTokenStore = new JwtTokenStore((JwtAccessTokenConverter) jwtAccessTokenConverter());
        return jwtTokenStore ;
    }


}