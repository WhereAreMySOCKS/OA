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
@RequestMapping("/admin/system/User")
@Api("用户管理接口")
public class SysUserController {
    @Autowired
    private SysUserService service;
    // 用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysUserQueryVo sysUserQueryVo){

        // 创建page对象
        Page<SysUser> pageParam = new Page<>(page,limit);
        // 封装条件，判断值不为空
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String username = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        if (StringUtils.isEmpty(username)){
            wrapper.like(SysUser::getUsername,username);
        }
        if (StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        if (StringUtils.isEmpty(createTimeEnd)){
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }
        IPage<SysUser> pageModel = service.page(pageParam, wrapper);
        // 调用mp的方法
        return Result.ok(pageModel);
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

