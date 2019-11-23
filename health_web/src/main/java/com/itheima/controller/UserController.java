package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/16 16:08
 * @Description: com.itheima.controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private MenuService menuService;
    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername() throws Exception{
        try {
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();
            List<Menu> menuList = menuService.findMenuByKeyword(username);
            HashMap<String, Object> map = new HashMap<>();
            map.put("menuList",menuList);
            map.put("username", username);
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,map);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
