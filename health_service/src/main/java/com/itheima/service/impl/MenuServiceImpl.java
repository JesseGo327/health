package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MenuDao;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/22 15:37
 * @Description: com.itheima.service.impl
 */
@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> findMenuByKeyword(String username) {
        return menuDao.findMenuByKeyword(username);
    }
}
