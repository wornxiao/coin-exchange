package com.worn.xiao.auth.constant;

public interface LoginConstant {

    /**
     * 管理员登录
     */
    public static final String ADMIN_TYPE = "admin_type" ;

    /**
     * 用户/会员登录
     */
    public static final String MEMBER_TYPE  = "member_type" ;

    // 用户名查用户
    public static final String QUERY_ADMIN_SQL =
            "SELECT `id` ,`username`, `password`, `status` FROM sys_user WHERE username = ? ";

    //判断用户是否管理员
    public static final String QUERY_ROLE_CODE_SQL =
            "SELECT `code` FROM sys_role LEFT JOIN sys_user_role ON sys_role.id = sys_user_role.role_id WHERE sys_user_role.user_id= ?";

    //管理员用户拥有所有权限
    public static final String QUERY_ALL_PERMISSIONS = "SELECT `name` FROM sys_privilege";

    //普通用户时（通过用户的角色查询用户的权限）
    public static final String QUERY_PERMISSION_SQL =
            "SELECT * FROM sys_privilege LEFT JOIN sys_role_privilege ON sys_role_privilege.privilege_id = sys_privilege.id LEFT JOIN sys_user_role  ON sys_role_privilege.role_id = sys_user_role.role_id WHERE sys_user_role.user_id = ?";

    public static final String QUERY_MEMBER_SQL =
            "SELECT `id`,`password`, `status` FROM `user` WHERE mobile = ? or email = ? ";

    String ADMIN_CODE = "ROLE_ADMIN";

    /**
     * token的刷新
     */
    public static  final  String REFRESH_TOKEN = "REFRESH_TOKEN" ;

    /**
     * 刷新token 纠正用户名
     */
    public static  final  String QUERY_ADMIN_USER_WITH_ID = "SELECT `username` FROM sys_user where id = ?" ;
    /**
     * 刷新token 纠正用户名
     */
    public static  final  String QUERY_MEMBER_USER_WITH_ID = "SELECT `mobile` FROM user where id = ?" ;
}
