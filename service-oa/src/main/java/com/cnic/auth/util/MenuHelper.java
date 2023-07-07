package com.cnic.auth.util;

import com.cnic.auth.service.SysMenuService;
import com.cnic.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> tree = new ArrayList<>();

        for(SysMenu sysMenu : sysMenuList){
            if(sysMenu.getParentId().longValue()==0){
                tree.add(getChildren(sysMenu,sysMenuList));
            }
        }
        return tree;
    }
    public static SysMenu getChildren(SysMenu sysMenu,
                                      List<SysMenu> sysMenuList){
        sysMenu.setChildren(new ArrayList<>());
        for (SysMenu it :sysMenuList) {
            if(sysMenu.getId().longValue() == it.getParentId().longValue()){
                if (sysMenu.getChildren()==null) sysMenu.setChildren(new ArrayList<>());
                sysMenu.getChildren().add(getChildren(it,sysMenuList));
            }
        }
        return sysMenu;
    }
}
