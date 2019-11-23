package com.itheima.dao;

import com.itheima.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;
import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/15 17:50
 * @Description: com.itheima.dao
 */
public interface OrderDao {

    /**
     * 动态条件查询
     * @param order
     * @return
     */
    @Select("<script>" +
            " select * from t_order " +
            "<where>" +
            "<if test='id != null'> and id = #{id} </if>" +
            "<if test='orderDate != null'> and orderDate = #{orderDate} </if>" +
            "<if test='orderType != null'> and orderType = #{orderType} </if>" +
            "<if test='orderStatus != null'> and orderStatus = #{orderStatus} </if>" +
            "<if test='packageId != null'> and package_id = #{packageId} </if>" +
            "</where>" +
            "</script>")
    public List<Order> findByCondition(Order order);

    /**
     * 保存预约信息到预约表
     * @param order
     */
    @Insert(" insert into t_order" +
            "(member_id,orderDate,orderType,orderStatus,package_id)" +
            "values" +
            "(#{memberId},#{orderDate},#{orderType},#{orderStatus},#{packageId})")
    @SelectKey(statement = "select LAST_INSERT_ID()",keyProperty = "id",before = false,resultType = Integer.class)
    void add(Order order);

    @Select("select m.name member ,s.name package,o.orderDate orderDate,o.orderType orderType" +
            "  from" +
            "  t_order o," +
            "  t_member m," +
            "  t_package s" +
            "  where o.member_id=m.id and o.package_id=s.id and o.id=#{id}")
    Map findById4Detail(Integer id);

    //今日预约数
    @Select("select count(id) from t_order where orderDate = #{value}")
    Integer findOrderCountByDate(String today);

    //本周预约数
    @Select("select count(id) from t_order where orderDate between #{thisWeekMonday} and #{thisWeekSunday}")
    Integer findOrderCountBetweenDate(@Param("thisWeekMonday") String thisWeekMonday, @Param("thisWeekSunday") String thisWeekSunday);

    //今日到诊数
    @Select("select count(id) from t_order where orderDate = #{value} and orderStatus = '已到诊'")
    Integer findVisitsCountByDate(String today);

    //本周到诊数
    @Select("select count(id) from t_order where orderDate >= #{value} and orderStatus = '已到诊'")
    Integer findVisitsCountAfterDate(String thisWeekMonday);

    //热门套餐
    @Select("SELECT p.`name`,COUNT(o.package_id) count,ROUND(COUNT(o.package_id)/(SELECT COUNT(1) FROM t_order)*100,2) proportion " +
            "FROM  t_order o,t_package p WHERE o.`package_id` = p.`id` GROUP BY o.package_id ORDER BY COUNT(o.package_id) DESC LIMIT 0,4;")
    List<Map> findHotPackage();
}
