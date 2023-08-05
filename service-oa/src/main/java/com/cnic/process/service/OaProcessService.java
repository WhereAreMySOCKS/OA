package com.cnic.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnic.model.process.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cnic.vo.process.ProcessQueryVo;
import com.cnic.vo.process.ProcessVo;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author Paul
 * @since 2023-08-05
 */
public interface OaProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageInfo, ProcessQueryVo processQueryVo);
}
