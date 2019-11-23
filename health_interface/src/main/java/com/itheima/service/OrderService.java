package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/15 17:23
 * @Description: com.itheima.service
 */
public interface OrderService {
    public Result order(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
