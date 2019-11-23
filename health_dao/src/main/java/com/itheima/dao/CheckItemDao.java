package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/7 17:44
 * @Description: com.itheima.dao
 */
public interface CheckItemDao {

    @Insert("insert into t_checkitem(code,name,sex,age,price,type,remark,attention) values (#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})")
    void add(CheckItem checkItem);


    @Select("<script>" +
            "select * from t_checkitem " +
            "<if test='value != null and value.length>0'>" +
            " where code like #{value} or name like #{value}" +
            "</if>" +
            "</script>")
    Page<CheckItem> findAllByCodition(String queryString);

    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id = #{id}")
    Long findCountByCheckItemId(Integer id);

    @Delete("delete from t_checkitem where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from t_checkitem where id = #{id}")
    CheckItem findById(Integer id);

    @Update("<script>" +
            "update t_checkitem " +
            "<set> " +
            "<if test='name != null'>" +
            " name = #{name}, " +
            "</if> " +
            "<if test='sex != null'>" +
            "sex = #{sex}, " +
            "</if> " +
            "<if test='code != null'>" +
            "  code = #{code}, " +
            "</if> " +
            "<if test='age != null'>" +
            "  age = #{age}, " +
            "</if> " +
            "<if test='price != null'>" +
            "  price = #{price}, " +
            "</if> " +
            "<if test='type != null'>" +
            "  type = #{type}, " +
            "</if> " +
            "<if test='attention != null'>" +
            "  attention = #{attention}, " +
            "</if> " +
            "<if test='remark != null'>" +
            "  remark = #{remark}, " +
            "</if> " +
            "</set> " +
            " where id = #{id} " +
            "</script>")
    void edit(CheckItem checkItem);

    /**
     * 查询所有检查项
     * @return
     */
    @Select("select * from t_checkitem")
    List<CheckItem> findAll();

    /**
     * 根据检查组id查询检查项信息
     * @param id
     * @return
     */
    @Select("select * from t_checkitem where id" +
            " in " +
            "(select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{id})")
    List<CheckItem> findCheckItemById(Integer id);
}
