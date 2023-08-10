package com.cnic.wechat.controller;

import com.cnic.common.result.Result;
import com.cnic.vo.wechat.MenuVo;
import com.cnic.wechat.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @ApiOperation(value = "获取全菜单")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo(){
        List<MenuVo> menuVoList = menuService.findMenuInfo();
        return Result.ok(menuVoList);
    }

    @ApiOperation(value = "同步菜单")
    @GetMapping("syncMenu")
    public Result createMenu(){
        menuService.syncMenu();
        return Result.ok();
    }

    @ApiOperation(value = "删除同步菜单")
    @DeleteMapping("removeMenu")
    public Result removeMenu(){
        menuService.removeMenu();
        return Result.ok();
    }

}