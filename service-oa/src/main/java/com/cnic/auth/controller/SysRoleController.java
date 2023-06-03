package com.cnic.auth.controller;

import com.cnic.auth.service.SysRoleService;
import com.cnic.common.result.Result;
import com.cnic.model.system.SysRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api("角色管理接口")
@RestController //注册controller，交给Spring管理，同时以json文件格式返回方法结果
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @ApiOperation("查询所有角色")
    @GetMapping("findAll") // http://localhost:8800/admin/system/sysRole/findAll
    public Result findAll(){
        List<SysRole> list = sysRoleService.list();
        // @RestController注解会将list转化成json格式返回
        return Result.ok(list);
    }
}
