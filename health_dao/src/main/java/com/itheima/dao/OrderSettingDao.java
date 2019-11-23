package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/12 10:16
 * @Description: com.itheima.dao
 */
public interface OrderSettingDao {

    /**
     * 查询预约日期
     * @param orderDate
     * @return
     */
    @Select("select count(*) from t_ordersetting where orderDate = #{orderDate}")
    Long findCountByOrderDate(Date orderDate);

    /**
     * 根据预约日期更新预约人数
     * @param orderSetting
     */
    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate}")
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 新增
     * @param orderSetting
     */
    @Insert("insert into t_ordersetting(orderDate,number,reservations) values(#{orderDate},#{number},#{reservations})")
    void add(OrderSetting orderSetting);

    /**
     * 根据年月查询预约信息
     * @param map
     * @return
     */
    @Select("select * from t_ordersetting where orderDate between #{dateBegin} and #{dateEnd}")
    List<OrderSetting> getOrderSettingByMonth(HashMap map);

    /**
     * 判断当前日期是否设置了预约
     * @param date
     * @return
     */
    @Select("select * from t_ordersetting where orderDate = #{date}")
    OrderSetting findByOrderDate(Date date);

    /**
     * 修改当日预约人数
     * @param orderSetting
     */
    @Update("update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate}")
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
