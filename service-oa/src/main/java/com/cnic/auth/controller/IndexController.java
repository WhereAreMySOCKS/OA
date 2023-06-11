package com.cnic.auth.controller;

import com.cnic.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 给前端返回相应的json数据，从vue-template 浏览器控制台中看到，所需json格式为
 * {"code":200,"data":{"token":"admin-token"}}
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    /**
     * login
     * @return
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(){
        //
        HashMap<String, Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }

    /**
     * info
     * @return
     */
    @GetMapping("/info")
    public Result info(){
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
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
