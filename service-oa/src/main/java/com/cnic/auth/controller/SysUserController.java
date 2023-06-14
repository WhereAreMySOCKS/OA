package com.cnic.auth.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnic.auth.service.SysUserService;
import com.cnic.common.result.Result;
import com.cnic.model.system.SysUser;
import com.cnic.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Paul
 * @since 2023-06-11
 */
@RestController
@RequestMapping("/admin/system/sysUser")
@Api(tags="用户管理接口")
public class SysUserController {
    @Autowired
    private SysUserService service;

    @ApiOperation("更改用户状态")
    @PostMapping("/updateStatus/{userId}/{status}")
    public Result doAssign(@PathVariable Long userId,@PathVariable Integer status){
        service.updateStatus(userId,status);
        return Result.ok();
    }
    // 用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysUserQueryVo sysUserQueryVo){

        Page<SysUser> sysUserPage = new Page<>(page, limit);
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 获取条件
        String userName = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        // 判断条件值不为空
        if (!StringUtils.isEmpty(userName)){
            lambdaQueryWrapper.like(SysUser::getUsername,userName);
        }
        if (!StringUtils.isEmpty(createTimeBegin)){
            lambdaQueryWrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)){
            lambdaQueryWrapper.le(SysUser::getCreateTime,createTimeEnd);
        }

        service.page(sysUserPage,lambdaQueryWrapper);
        return Result.ok(sysUserPage);
    }
    @ApiOperation("获取用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable long id){
        SysUser user = service.getById(id);
        return Result.ok(user);
    }

    @ApiOperation("更新用户")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser){
        boolean is_success = service.updateById(sysUser);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("保存用户")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser){
        boolean is_success = service.save(sysUser);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable long id) {
        boolean is_success = service.removeById(id);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}

