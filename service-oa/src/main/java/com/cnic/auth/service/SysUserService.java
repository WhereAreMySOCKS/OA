package com.cnic.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cnic.model.system.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Paul
 * @since 2023-06-11
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long userId,  Integer status);
}
