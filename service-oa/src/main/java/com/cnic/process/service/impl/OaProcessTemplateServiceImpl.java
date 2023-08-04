package com.cnic.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnic.model.process.ProcessTemplate;
import com.cnic.model.process.ProcessType;
import com.cnic.process.mapper.OaProcessTemplateMapper;
import com.cnic.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author Paul
 * @since 2023-08-04
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {
    @Autowired
    private OaProcessTypeServiceImpl processTypeService;
    @Override
    public IPage<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pageInfo) {
        // 分页查询
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageInfo,null);
        // 遍历返回结果，获得审批类型id
        List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();
        // 根据id获取名称，完成封装
        for (ProcessTemplate processTemplate : processTemplateList){
            Long processTypeId = processTemplate.getProcessTypeId();
            ProcessType processType = processTypeService.getById(processTypeId);
            if(processType == null) continue;
            processTemplate.setProcessTypeName(processType.getName());
        }
        return processTemplatePage;
    }
}
