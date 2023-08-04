package com.cnic.process.service.impl;

import com.cnic.model.process.ProcessType;
import com.cnic.process.mapper.OaProcessTypeMapper;
import com.cnic.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author Paul
 * @since 2023-08-04
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

}
