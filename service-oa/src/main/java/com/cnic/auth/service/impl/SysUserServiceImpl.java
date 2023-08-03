package com.cnic.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cnic.auth.mapper.SysUserMapper;
import com.cnic.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cnic.model.system.SysUser;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Paul
 * @since 2023-06-11
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public void updateStatus(Long userId, Integer status) {
        // 实现修改用户状态功能
        // 根据userId查询用户
        SysUser sysUser = baseMapper.selectById(userId);
        sysUser.setStatus(status);
        baseMapper.updateById(sysUser);



    }

    @Override
    public SysUser getUserByUserName(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return baseMapper.selectOne(wrapper);
    }
}
