package com.cnic.process.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnic.model.process.Process;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cnic.vo.process.ProcessQueryVo;
import com.cnic.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author Paul
 * @since 2023-08-05
 */
@Mapper
public interface OaProcessMapper extends BaseMapper<Process> {
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageInfo, @Param("vo") ProcessQueryVo processQueryVo);
}
