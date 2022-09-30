package com.worn.xiao.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.worn.xiao.domain.WorkIssue;
import com.worn.xiao.mapper.WorkIssueMapper;
import com.worn.xiao.service.WorkIssueService;
@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService{

}
