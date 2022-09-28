package com.worn.xiao.auth.service;

import com.worn.xiao.auth.constant.LoginConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String loginType = requestAttributes.getRequest().getParameter("login_type");
        if (StringUtils.isEmpty(loginType)) {
            throw new AuthenticationServiceException("请添加login_type参数");
        }
        if (LoginConstant.REFRESH_TOKEN.equals(loginType.toUpperCase())) {
            username = adjustUsername(username, loginType); // 为refresh_token 时，需要将id->username
        }
        UserDetails userDetails = null;
        switch (loginType) {
            case LoginConstant.ADMIN_TYPE: // 管理员登录
                userDetails = loadAdminUserByUsername(username);
                break;
            case LoginConstant.MEMBER_TYPE: // 会员登录
                userDetails = loadMemberUserByUsername(username);
                break;
            default:
                throw new AuthenticationServiceException("暂不支持的登录方式" + loginType);
        }
        return userDetails;
    }

    private String adjustUsername(String username, String loginType) {
        if (LoginConstant.ADMIN_TYPE.equals(loginType)) {
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_ADMIN_USER_WITH_ID, String.class, username);
        }
        if (LoginConstant.MEMBER_TYPE.equals(loginType)) {
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_USER_WITH_ID, String.class, username);
        }
        return username;
    }

    private UserDetails loadMemberUserByUsername(String username) {
        return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_SQL, new RowMapper<UserDetails>() {
            @Override
            public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                if(rs.wasNull()){
                    throw new UsernameNotFoundException("会员：" + username + "不存在");
                }
                long id = rs.getLong("id"); // 获取用户的id
                String password = rs.getString("password");
                int status = rs.getInt("status");
                return new User(
                        String.valueOf(id),
                        password,
                        status == 1 ,
                        true ,
                        true ,
                        true,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            }
        }, username, username);
    }

    private Set<SimpleGrantedAuthority> getUserPermissions(Long id) {
        // 查询用户是否为管理员
        String code = jdbcTemplate.queryForObject(LoginConstant.QUERY_ROLE_CODE_SQL, String.class, id);
        List<String> permissions = null;
        if (LoginConstant.ADMIN_CODE.equals(code)) { // 管理员
            permissions = jdbcTemplate.queryForList(LoginConstant.QUERY_ALL_PERMISSIONS, String.class);
        } else {
            permissions = jdbcTemplate.queryForList(LoginConstant.QUERY_PERMISSION_SQL, String.class, id);
        }
        if (permissions.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        return permissions
                .stream()
                .distinct() // 去重
                .map(
                        SimpleGrantedAuthority::new // perm - >security可以识别的权限
                )
                .collect(Collectors.toSet());
    }

    private UserDetails loadAdminUserByUsername(String username) {
        return jdbcTemplate.queryForObject(LoginConstant.QUERY_ADMIN_SQL, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs.wasNull()) {
                    throw new UsernameNotFoundException("用户：" + username + "不存在");
                }
                Long id = rs.getLong("id");
                String password = rs.getString("password");
                int status = rs.getInt("status");
                User user = new User(
                        String.valueOf(id), // 使用用户的id 代替用户的名称，这样会使得后面的很多情况得以处理
                        password,
                        status == 1,
                        true,
                        true,
                        true,
                        getUserPermissions(id));
                return user;
            }
        }, username);
    }
}
