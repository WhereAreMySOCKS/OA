package com.cnic.auth.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cnic.model.system.SysUser;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long userId,  Integer status);

    SysUser getUserByUserName(String username);

    Map<String, Object> getCurrentUser();
}
