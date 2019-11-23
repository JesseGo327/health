package com.itheima.service;

import com.itheima.pojo.User;

/**
 * @Author: tangx
 * @Date: 2019/11/16 15:43
 * @Description: com.itheima.service
 */
public interface UserService {
    User findUserByUserName(String username);
}
