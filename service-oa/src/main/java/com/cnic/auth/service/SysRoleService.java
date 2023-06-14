package com.cnic.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cnic.model.system.SysRole;
import com.cnic.vo.system.AssginRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    void doAssign(AssginRoleVo assginRoleVo);

    Map<String, Object> findRoleDataByUserId(Long userId);
}
