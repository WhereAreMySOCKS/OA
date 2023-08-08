package com.cnic.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnic.model.process.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cnic.vo.process.ApprovalVo;
import com.cnic.vo.process.ProcessFormVo;
import com.cnic.vo.process.ProcessQueryVo;
import com.cnic.vo.process.ProcessVo;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

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
    void deployByZip(String deployPath);

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    void approve(ApprovalVo approvalVo);

    Map<String, Object> show(Long id);

}
