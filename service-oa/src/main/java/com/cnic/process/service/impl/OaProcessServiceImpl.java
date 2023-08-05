package com.cnic.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnic.model.process.Process;
import com.cnic.process.mapper.OaProcessMapper;
import com.cnic.process.service.OaProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cnic.vo.process.ProcessQueryVo;
import com.cnic.vo.process.ProcessVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author Paul
 * @since 2023-08-05
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageInfo, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageInfo, processQueryVo);
        return pageModel;
    }
}
