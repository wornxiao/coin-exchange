package com.worn.xiao.controller;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.common.constant.R;
import com.worn.xiao.domain.*;
import com.worn.xiao.dto.UserDto;
import com.worn.xiao.feign.UserServiceFeign;
import com.worn.xiao.service.UserAuthAuditRecordService;
import com.worn.xiao.service.UserAuthInfoService;
import com.worn.xiao.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Api(tags = "会员的控制器")
public class UserController implements UserServiceFeign {


    @Autowired
    private UserService userService ;

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @GetMapping("/auths")
    @ApiOperation(value = "查询用户的审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
            @ApiImplicitParam(name = "mobile", value = "会员的手机号"),
            @ApiImplicitParam(name = "userId", value = "会员的Id,精确查询"),
            @ApiImplicitParam(name = "userName", value = "会员的名称"),
            @ApiImplicitParam(name = "realName", value = "会员的真实名称"),
            @ApiImplicitParam(name = "reviewsStatus", value = "会员的状态")

    })
    public R<Page<User>> findUserAuths(
            @ApiIgnore Page<User> page,
            String mobile,
            Long userId,
            String userName,
            String realName,
            Integer reviewsStatus
    ) {
        Page<User> userPage = userService.findByPage(page, mobile, userId, userName, realName, null, reviewsStatus);
        return R.ok(userPage);
    }


    @PostMapping("/status")
    @ApiOperation(value = "修改用户的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "会员的id") ,
            @ApiImplicitParam(name = "status" ,value = "会员的状态") ,
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(Long id ,Byte status){
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        boolean updateById = userService.updateById(user);
        if(updateById){
            return R.ok("更新成功") ;
        }
        return R.fail("更新失败") ;
    }


    @PatchMapping
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user" ,value = "会员的json数据") ,
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(@RequestBody @Validated User user){
        boolean updateById = userService.updateById(user);
        if(updateById){
            return R.ok("更新成功") ;
        }
        return R.fail("更新失败") ;
    }

    @GetMapping("/directInvites")
    @ApiOperation(value = "查询该用户邀请的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId" ,value = "该用户的Id"),
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),

    })
    public R<Page<User>> getDirectInvites(@ApiIgnore Page<User> page , Long userId){
        Page<User> userPage = userService.findDirectInvitePage(page ,userId)  ;
        return R.ok(userPage) ;
    }


    /**
     * 询用户的认证详情
     * {
     * user:
     * userAuthInfoList:[]
     * userAuditRecordList:[]
     * }
     */
    @GetMapping("/auth/info")
    @ApiOperation(value = "查询用户的认证详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的Id")
    })
    public R<UseAuthInfoVo> getUseAuthInfo(Long id) {
        User user = userService.getById(id);
        List<UserAuthAuditRecord> userAuthAuditRecordList = null;
        List<UserAuthInfo> userAuthInfoList = null;
        if (user != null) {
            // 用户的审核记录
            Integer reviewsStatus = user.getReviewsStatus();
            if (reviewsStatus == null || reviewsStatus == 0) { // 待审核
                userAuthAuditRecordList = Collections.emptyList(); // 用户没有审核记录
                //
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByUserId(id);
            } else {
                userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(id);
                // 查询用户的认证详情列表-> 用户的身份信息
                UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordList.get(0);// 之前我们查询时,是按照认证的日志排序的,第0 个值,就是最近被认证的一个值
                Long authCode = userAuthAuditRecord.getAuthCode(); // 认证的唯一标识
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByCode(authCode);
            }
        }
        return R.ok(new UseAuthInfoVo(user, userAuthInfoList, userAuthAuditRecordList));
    }


    /**
     * 审核的本质:
     * 在于对一组图片(唯一Code)的认可,符合条件,审核通过
     *
     * @return
     */
    @PostMapping("/auths/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的ID"),
            @ApiImplicitParam(name = "authStatus", value = "用户的审核状态"),
            @ApiImplicitParam(name = "authCode", value = "一组图片的唯一标识"),
            @ApiImplicitParam(name = "remark", value = "审核拒绝的原因"),
    })
    public R updateUserAuthStatus(@RequestParam(required = true) Long id, @RequestParam(required = true) Byte authStatus, @RequestParam(required = true) Long authCode, String remark) {
        // 审核: 1 修改user 里面的reviewStatus
        // 2 在authAuditRecord 里面添加一条记录

        userService.updateUserAuthStatus(id, authStatus, authCode, remark);

        return R.ok();
    }


    @GetMapping("/current/info")
    @ApiOperation(value = "获取当前登录用户的详情")
    public R<User> currentUserInfo(){
        // 获取用户的Id
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getById(Long.valueOf(idStr));
        if(user==null){
            throw new IllegalArgumentException("请输入正确的用户Id") ;
        }
        Byte seniorAuthStatus = null ; // 用户的高级认证状态
        String seniorAuthDesc = "" ;// 用户的高级认证未通过,原因
        Integer reviewsStatus = user.getReviewsStatus(); // 用户被审核的状态 1通过,2拒绝,0,待审核"
        if(reviewsStatus == null){ //为null 时,代表用户的资料没有上传
            seniorAuthStatus = 3 ;
            seniorAuthDesc = "资料未填写" ;
        }else {
            switch (reviewsStatus) {
                case 1:
                    seniorAuthStatus = 1;
                    seniorAuthDesc = "审核通过";
                    break;
                case 2:
                    seniorAuthStatus = 2;
                    seniorAuthDesc = "原因未知";
                    // 查询被拒绝的原因--->审核记录里面的
                    // 时间排序,
                    List<UserAuthAuditRecord> userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(user.getId());
                    if (!CollectionUtils.isEmpty(userAuthAuditRecordList)) {
                        UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordList.get(0);
                        seniorAuthDesc = userAuthAuditRecord.getRemark();
                    }
                    break;
                case 0:
                    seniorAuthStatus = 0;
                    seniorAuthDesc = "等待审核";
                    break;
            }
        }
        user.setSeniorAuthStatus(seniorAuthStatus);
        user.setSeniorAuthDesc(seniorAuthDesc);
        return R.ok(user) ;
    }


    @PostMapping("/authAccount")
    @ApiOperation(value = "用户的实名认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "" ,value = "")
    })
    public R identifyCheck(@RequestBody UserAuthForm userAuthForm){
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.identifyVerify(Long.valueOf(idStr),userAuthForm) ;
        if(isOk){
            return R.ok() ;
        }
        return R.fail("认证失败") ;
    }

    @PostMapping("/authUser")
    @ApiOperation(value = "用户进行高级认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgs",value ="用户的图片地址" )
    })
    public  R authUser(@RequestBody  String []imgs){
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.authUser(Long.valueOf(idStr), Arrays.asList(imgs)) ;
        return R.ok() ;
    }

    @PostMapping("/updatePhone")
    @ApiOperation(value = "修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updatePhoneParam",value = "updatePhoneParam 的json数据")
    })
    public R updatePhone(@RequestBody  UpdatePhoneParams updatePhoneParam){
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk =  userService.updatePhone(Long.valueOf(idStr),updatePhoneParam) ;
        if(isOk){
            return R.ok() ;
        }
        return R.fail("修改失败") ;
    }


    @GetMapping("/checkTel")
    @ApiOperation(value = "检查新的手机号是否可用,如可用,则给该新手机发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile" ,value = "新的手机号"),
            @ApiImplicitParam(name = "countryCode" ,value = "手机号的区域")
    })
    public R checkNewPhone(@RequestParam(required = true) String mobile,@RequestParam(required = true) String countryCode){
        boolean isOk =   userService.checkNewPhone(mobile,countryCode) ;
        return isOk ? R.ok():R.fail("新的手机号校验失败") ;
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户的注册")
    public R register(@RequestBody RegisterParam registerParam) {
        boolean isOk = userService.register(registerParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("注册失败");
    }

    @Override
    public Map<Long, UserDto> getBasicUsers(List<Long> ids, String username, String mobile) {
        List<UserDto> basicUsers = userService.getBasicUsers(ids);
        return basicUsers.stream().collect(Collectors.toMap(UserDto::getId,p->p));
    }
}