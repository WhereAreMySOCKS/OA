package com.cnic.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cnic.model.wechat.Menu;
import com.cnic.vo.wechat.MenuVo;
import com.cnic.wechat.mapper.MenuMapper;
import com.cnic.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private WxMpService wxMpService;
    @Override
    public List<MenuVo> findMenuInfo() {
        List<MenuVo> result = new ArrayList<>();
        // 查询所有菜单
        List<Menu> menus = baseMapper.selectList(null);
        // 查询所有一级菜单（parent_id = 0）
        List<Menu> oneMenuList = menus.stream().filter(menu -> menu.getParentId().longValue()==0).collect(Collectors.toList());
        //  遍历
        for(Menu oneMenu: oneMenuList){
            // 一级菜单Menu转成MeanVo
            MenuVo oneMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu,oneMenuVo);
            // 一级菜单的id和其他菜单的parent_id相同，说明其为该菜单的二忌菜单
            List<Menu> twoMenuList = menus.stream().filter(menu -> menu.getParentId().longValue() == oneMenuVo.getId()).collect(Collectors.toList());
            List<MenuVo>   children = new ArrayList<>();
            for(Menu twoMenu:twoMenuList){
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu,twoMenuVo);
                children.add(twoMenuVo);
            }
            oneMenuVo.setChildren(children);
            result.add(oneMenuVo);
        }
//        // 能不能用递归？
        return result;
    }


    //同步菜单
    @Override
    public void syncMenu() {
        //1 菜单数据查询出来，封装微信要求菜单格式
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for(MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if(CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://cnicoa.v1.idcfengye.com/#"+oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for(MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if(twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        //H5页面地址
                        view.put("url", "http://cnicoa.v1.idcfengye.com#"+twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        //菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);

        //2 调用工具里面的方法实现菜单推送
        try {
            wxMpService.getMenuService().menuCreate(button.toString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }

    }

    //删除菜单
    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
