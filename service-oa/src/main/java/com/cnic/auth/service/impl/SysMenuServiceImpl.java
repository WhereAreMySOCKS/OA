package com.cnic.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cnic.auth.service.SysRoleMenuService;
import com.cnic.auth.util.MenuHelper;
import com.cnic.common.config.exception.MyExceptionHandler;
import com.cnic.model.system.SysMenu;
import com.cnic.auth.mapper.SysMenuMapper;
import com.cnic.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cnic.model.system.SysRoleMenu;
import com.cnic.vo.system.AssginMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Paul
 * @since 2023-06-15
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Override
    public List<SysMenu> findNodes() {
        // 递归的查询所有层级数据
        // 1 查询所有数据
        List<SysMenu> sysMenus = baseMapper.selectList(null);
        // 2 构建树形结构
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenus);

        return resultList;
    }

    @Override
    public void removeMenuById(Long id) {
        // 判断存在子菜单时，删除失败
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId,id); // 当表中的任意数据patentId = id，说明当前id有子菜单，无法删除
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            throw new MyExceptionHandler(201,"当前菜单存在子菜单，删除失败");
        }
        baseMapper.deleteById(id);


    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        // 1. 查询所有状态码为1的菜单
        LambdaQueryWrapper<SysMenu> sysMenuWrapper = new LambdaQueryWrapper<>();
        sysMenuWrapper.eq(SysMenu::getStatus,1);
        List<SysMenu> allSysMenuList = baseMapper.selectList(sysMenuWrapper);
        // 2. 根据角色id查询对应的菜单id
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuWrapper.eq(SysRoleMenu::getMenuId,roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(sysRoleMenuWrapper);
        List<Long> menueIdList = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        // 3. 根据菜单id，从菜单集合（第一步获取的）获得对应的菜单对象
        // setSelect 设置"是否选中"标志位
        allSysMenuList.forEach(item -> {
            item.setSelect(menueIdList.contains(item.getId()));
        });

        return MenuHelper.buildTree(allSysMenuList);
    }

    // 角色分配菜单
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        // 1. 删除已有的记录
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getMenuId,assginMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);
        // 2. 分配新的菜单
        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for(Long menuId:menuIdList){
            if(StringUtils.isEmpty(menuId)) continue;
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }
}
