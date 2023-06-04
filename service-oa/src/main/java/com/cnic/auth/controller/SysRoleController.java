package com.cnic.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnic.auth.service.SysRoleService;
import com.cnic.common.result.Result;
import com.cnic.model.system.SysRole;
import com.cnic.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("分页查询")
    @GetMapping("{page}/{limit}")
    // 参数分别为 当前页，每页数，页对象
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {

        // 创建Page对象，传递分页参数
        Page<SysRole> pageParam = new Page<>(page,limit);
        // 封装条件 判断条件是否为空
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if(!StringUtils.isEmpty(roleName)){
            wrapper.like(SysRole::getRoleName,roleName);
        }
        IPage<SysRole> iPage = sysRoleService.page(pageParam,wrapper);
        return Result.ok(iPage);
    }

    @ApiOperation("添加角色")
    @PostMapping("save")
    // @RequestBody通过json数据传送数据，使用该注解应该使用post提交
    public Result save(@RequestBody SysRole sysRole) {
        boolean isSuccess = sysRoleService.save(sysRole);
        if (isSuccess) return Result.ok();
        else return Result.fail();
    }

    @ApiOperation("根据id查询")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result get(@RequestBody SysRole sysRole) {
        boolean isSuccess = sysRoleService.updateById(sysRole);
        if (isSuccess) return Result.ok();
        else return Result.fail();
    }
    @ApiOperation("id删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean isSuccess = sysRoleService.removeById(id);
        if (isSuccess) return Result.ok();
        else return Result.fail();
    }
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    // 传入数组格式
    public Result batchRemove(@RequestBody List<Long> ids) {
        boolean isSuccess = sysRoleService.removeByIds(ids);
        if (isSuccess) return Result.ok();
        else return Result.fail();
    }
}
