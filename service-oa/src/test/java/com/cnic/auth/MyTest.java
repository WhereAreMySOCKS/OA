package com.cnic.auth;

import com.cnic.auth.mapper.SysRoleMapper;
import com.cnic.auth.service.SysRoleService;
import com.cnic.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MyTest {
    @Autowired
    private SysRoleMapper mapper;
    @Autowired
    private SysRoleService service;
    @Test
    public void getAll(){
        List<SysRole> list =  mapper.selectList(null);
        System.out.println(list);
    }

    @Test
    public void testAdd(){
        SysRole role = new SysRole();
        role.setRoleName("管理员");
        role.setRoleCode("role");
        role.setDescription("2");
        int list =  mapper.insert(role);
        System.out.println(list);
    }

    @Test
    public void testSerivce(){
        List<SysRole> list = service.list();
        System.out.println(list);

    }
}
