package com.cnic.auth.service;

import com.cnic.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cnic.model.system.SysUser;
import com.cnic.vo.system.AssginMenuVo;
import com.cnic.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Paul
 * @since 2023-06-15
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findNodes();
    void removeMenuById(Long id);
    List<SysMenu> findMenuByRoleId(Long roleId);
    void doAssign(AssginMenuVo assginMenuVo);
    List<RouterVo> findUserMenuListByUserId(Long userId);
    List<String> findUserPermsByUserId(Long userId);
}
