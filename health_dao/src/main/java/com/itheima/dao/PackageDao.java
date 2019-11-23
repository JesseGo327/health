package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Package;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/11 15:11
 * @Description: com.itheima.dao
 */
public interface PackageDao {

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    @Select("<script>" +
            " select * from t_package " +
            "<if test='value != null and value.length>0'>" +
            " where name like #{value} or code like #{value} or helpCode like #{value} " +
            "</if>" +
            "</script>")
    Page<Package> findPage(String queryString);

    /**
     * 新增掏槽
     * @param pack
     */
    @Insert("insert into t_package" +
            "(name,code,helpCode,sex,age,price,remark,attention,img) " +
            "values(#{name},#{code},#{helpCode},#{sex},#{age},#{price},#{remark},#{attention},#{img})")
    @SelectKey(statement = "select LAST_INSERT_ID()",keyProperty = "id",before = false,resultType = int.class)
    void add(Package pack);

    /**
     * 绑定套餐和检查组的多对多关系
     * @param map
     */
    @Insert("insert into t_package_checkgroup values(#{package_id},#{checkgroup_id})")
    void setPackageAndCheckGroup(Map<String, Integer> map);

    /**
     * 查询所有套餐
     * @return
     */
    @Select("select * from t_package")
    List<Package> findAll();

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from t_package where id = #{id}")
    @Results({
            @Result(column = "id",property = "checkGroups",many = @Many(
                    select = "com.itheima.dao.CheckGroupDao.findCheckGroupById"
            ))
    })
    Package findById(Integer id);

    @Select("SELECT p.name,COUNT(*) value FROM t_package p,t_order o WHERE p.`id` = o.`package_id` GROUP BY p.`name`")
    List<Map<String, String>> findPackageCount();
}
