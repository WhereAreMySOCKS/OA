package com.cnic.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cnic.auth.mapper.SysRoleMapper;
import com.cnic.auth.service.SysRoleService;
import com.cnic.model.system.SysRole;
import org.springframework.stereotype.Service;

@Service
//继承ServiceImpl，其中自动注入了mapper
public class SysRolServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
