package com.itheima.service;

import com.itheima.pojo.Menu;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/22 15:35
 * @Description: com.itheima.service
 */
public interface MenuService {
    /**
     * 动态展示mian菜单
     */
    List<Menu> findMenuByKeyword(String username);
}
