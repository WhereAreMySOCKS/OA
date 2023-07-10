package com.cnic.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cnic.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author Paul
 * @since 2023-06-15
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findMenuListByUserId(@Param("userId") Long userId);
}
