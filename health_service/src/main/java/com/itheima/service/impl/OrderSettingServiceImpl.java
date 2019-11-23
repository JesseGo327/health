package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置服务
 * @Author: tangx
 * @Date: 2019/11/12 10:17
 * @Description: com.itheima.service.impl
 */

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao ordersettingDao;

    //批量添加
    @Override
    @Transactional
    public void add(ArrayList<OrderSetting> list) {
        if (list !=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                //检查此数据日期是否存在
               Long count = ordersettingDao.findCountByOrderDate(orderSetting.getOrderDate());
               if (count>0){
                   //已经存在,执行更新操作
                   ordersettingDao.editNumberByOrderDate(orderSetting);
               }else {
                   //不存在,执行添加操作
                   ordersettingDao.add(orderSetting);
               }
            }
        }
    }

    /**
     * 根据年月获取预约信息
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String dateBegin = date+"-1";
        String dateEnd = date + "-31";
        HashMap map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        List<OrderSetting> list = ordersettingDao.getOrderSettingByMonth(map);
        ArrayList<Map> data = new ArrayList();
        for (OrderSetting orderSetting : list) {
            HashMap orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number",orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        return data;
    }

    /**
     * 修改预约信息
     * @param orderSetting
     */
    @Override
    @Transactional
    public void editNumberByDate(OrderSetting orderSetting) {
        Long countByOrderDate = ordersettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (countByOrderDate>0){
            if (countByOrderDate == orderSetting.getNumber()){
                //设置数量与数据库一致,不需要更新
                return ;
            }
            //当前已经进行了预约设置,进行修改操作
            ordersettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //当日还没有预约,进行新增操作
            ordersettingDao.add(orderSetting);
        }
    }
}
