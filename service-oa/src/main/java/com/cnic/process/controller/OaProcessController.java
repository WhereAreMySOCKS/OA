package com.cnic.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnic.common.result.Result;
import com.cnic.process.service.OaProcessService;
import com.cnic.vo.process.ProcessQueryVo;
import com.cnic.vo.process.ProcessVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/admin/process")
public class OaProcessController {
    @Autowired
    private OaProcessService processService;

    //审批管理列表
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        ProcessQueryVo processQueryVo) {
        Page<ProcessVo> pageInfo = new Page<>(page, limit);
        IPage<ProcessVo> pageModel =  processService.selectPage(pageInfo,processQueryVo);
        return Result.ok(pageModel);
    }
}

