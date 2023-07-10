package com.cnic.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cnic.auth.service.SysMenuService;
import com.cnic.auth.service.SysUserService;
import com.cnic.common.config.exception.MyException;
import com.cnic.common.jwt.JwtHelper;
import com.cnic.common.result.Result;
import com.cnic.common.utils.MD5;
import com.cnic.model.system.SysUser;
import com.cnic.vo.system.LoginVo;
import com.cnic.vo.system.RouterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 给前端返回相应的json数据，从vue-template 浏览器控制台中看到，所需json格式为
 * {"code":200,"data":{"token":"admin-token"}}
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysMenuService sysMenuService;
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        // 获取输入的用户名和密码
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser one = sysUserService.getOne(wrapper);
        // 在数据库中核对
        if(one == null){
            throw new MyException(201,"用户不存在");
        }
        if(!MD5.encrypt(loginVo.getPassword()).equals(one.getPassword())){
            throw new MyException(201,"密码错误");
        }
        if (one.getStatus() == 0){
            throw new MyException(201,"用户被禁用");
        }
        // 使用jwt根据用户id和用户名称生成token
        String token = JwtHelper.createToken(one.getId(), one.getUsername());
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }

    /**
     * info
     * @return
     */
    @GetMapping("/info")
    public Result info(HttpServletRequest request){
        // 从请求头中获取token中记录的userId
        String token = request.getHeader("header");
        // 根据userId查询用户信息，分配对应权限
        Long userId = 2L;//JwtHelper.getUserId(token);
        SysUser sysUser = sysUserService.getById(userId);
        List<RouterVo> routerVoList =  sysMenuService.findUserMenuListByUserId(userId);
        List<String> permsVoList = sysMenuService.findUserPermsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("routers",routerVoList);
        map.put("buttons",permsVoList);
        return Result.ok(map);
    }

    /**
     * logout
     * @return
     */
    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
