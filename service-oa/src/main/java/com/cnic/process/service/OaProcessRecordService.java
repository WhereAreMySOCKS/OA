package com.cnic.process.service;

import com.cnic.model.process.ProcessRecord;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author Paul
 * @since 2023-08-09
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {
    void record(Long processId,Integer status,String description);
}
