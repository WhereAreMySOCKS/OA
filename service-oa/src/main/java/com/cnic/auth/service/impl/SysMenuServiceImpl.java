package com.cnic.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cnic.auth.service.SysRoleMenuService;
import com.cnic.auth.util.MenuHelper;
import com.cnic.common.config.exception.MyException;
import com.cnic.model.system.SysMenu;
import com.cnic.auth.mapper.SysMenuMapper;
import com.cnic.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cnic.model.system.SysRoleMenu;
import com.cnic.vo.system.AssginMenuVo;
import com.cnic.vo.system.MetaVo;
import com.cnic.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
            throw new MyException(201,"当前菜单存在子菜单，删除失败");
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
        sysRoleMenuWrapper.eq(SysRoleMenu::getRoleId,roleId);
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

    // 通过userId查询对应的菜单页面
    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        // 1. 约定userId = 1为管理员，获得所有菜单页面。
        if(userId == 1){
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        }else{
            // 不是管理员则查询可以操作的菜单页面
            sysMenuList = baseMapper.findMenuListByUserId(userId);

        }
        // 2. 查询的结果封装成前端框架所需的格式
        // 2.1 构建树形结构
        List<SysMenu> sysMenusTree = MenuHelper.buildTree(sysMenuList);
        return this.buildRouter(sysMenusTree);
    }
    public List<RouterVo> buildRouter(List<SysMenu> menus){
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenu menu:menus
             ) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));

            List<SysMenu> children = menu.getChildren();
            if(menu.getType() == 1) {
                List<SysMenu> hiddenMunuList = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());

                for(SysMenu hiddenMenu : hiddenMunuList){
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            }else {
                if(!CollectionUtils.isEmpty(children)){
                    if (children.size()>0) router.setAlwaysShow(true);
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    // 通过userId查询按钮权限，类似findUserMenuListByUserId方法
    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        // admin拥有所有权限
        if(userId == 1){
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            sysMenuList = baseMapper.selectList(wrapper);
        }else{
            // 不是管理员则查询可以操作的菜单页面
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }

        return sysMenuList.stream().filter(item -> item.getType() == 2)
                .map(SysMenu::getPerms).collect(Collectors.toList());
    }
}
