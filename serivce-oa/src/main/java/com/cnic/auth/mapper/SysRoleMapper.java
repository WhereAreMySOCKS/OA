package com.cnic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cnic.model.system.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
}
