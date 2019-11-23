package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/12 10:17
 * @Description: com.itheima.service
 */
public interface OrderSettingService {
    void add(ArrayList<OrderSetting> orderSettings);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
