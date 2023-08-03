package com.cnic.auth.service.impl;
import com.cnic.auth.service.SysMenuService;
import com.cnic.auth.service.SysUserService;
import com.cnic.model.system.SysUser;
import com.cnic.security.custom.CustomUser;
import com.cnic.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据用户名查询
        SysUser sysUser = sysUserService.getUserByUserName(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }
        List<String> userPermsByUserId = sysMenuService.findUserPermsByUserId(sysUser.getId());
        ArrayList<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String prem : userPermsByUserId) {
            authList.add(new SimpleGrantedAuthority(prem.trim()));
        }
        return new CustomUser(sysUser, authList);
    }
}
