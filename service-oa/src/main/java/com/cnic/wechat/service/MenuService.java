package com.cnic.wechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cnic.model.wechat.Menu;
import com.cnic.vo.wechat.MenuVo;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
