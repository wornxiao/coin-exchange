package com.worn.xiao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseAuthInfoVo {
    private User user;
    private List<UserAuthInfo> userAuthInfoList;
    private List<UserAuthAuditRecord> userAuthAuditRecords;
}
