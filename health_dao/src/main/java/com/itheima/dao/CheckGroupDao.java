package com.itheima.dao;

import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/10 16:13
 * @Description: com.itheima.dao
 */
public interface CheckGroupDao {

    /**
     * 新增检查组
     * @param checkGroup
     */
    @Insert("insert into t_checkgroup (code,name,sex,helpCode,remark,attention) values (#{code},#{name},#{sex},#{helpCode},#{remark},#{attention})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()",
            keyProperty = "id",
            before = false,
            resultType = Integer.class)
    void add(CheckGroup checkGroup);

    /**
     * 设置检查组和检查项的关联关系
     * @param map
     */
    @Insert("insert into t_checkgroup_checkitem(checkgroup_id,checkitem_id) values (#{checkgroup_id},#{checkitem_id})")
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    @Select("<script>" +
            " select * from t_checkgroup " +
            "<if test='value != null and value.length > 0'>" +
            "where code like #{value} or name like #{value} or helpCode like #{value}" +
            "</if>" +
            "</script>")
    List<CheckGroup> findPage(String queryString);

    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    @Select("select * from t_checkgroup where id = #{id}")
    CheckGroup findById(Integer id);

    /**
     * 根据检查组合id查询对应的所有检查项id
     * @param id
     * @return
     */
    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{id}")
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 根据检查组id删除中间表数据(清理原有关联关系)
     * @param id
     */
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{id}")
    void deleteAssociation(Integer id);

    /**
     * 更新检查组基本信息
     * @param checkGroup
     */
    @Update("<script>" +
            " update t_checkgroup" +
            "<set><if test=\"name != null\">" +
            " name = #{name}, " +
            "</if><if test=\"sex != null\">" +
            " sex = #{sex}, " +
            "</if><if test=\"code != null\">" +
            " code = #{code}, " +
            "</if><if test=\"helpCode != null\">" +
            " helpCode = #{helpCode}, " +
            "</if><if test=\"attention != null\">" +
            " attention = #{attention}, " +
            "</if><if test=\"remark != null\">" +
            " remark = #{remark}, " +
            "</if></set>" +
            " where id = #{id} " +
            "</script>")
    void edit(CheckGroup checkGroup);

    /**
     * 删除检查组
     * @param id
     */
    @Delete("delete from t_checkgroup where id = #{id}")
    void deleteById(Integer id);

    /**
     * 查询所有检查组
     * @return
     */
    @Select("select * from t_checkgroup")
    List<CheckGroup> findAll();

    /**
     *根据套餐id查询检查项信息
     */
    @Select("select * from t_checkgroup where id " +
            "in " +
            "(select checkgroup_id from t_package_checkgroup where package_id=#{id})")
    @Results({
            @Result(column = "id",property = "checkItems",
                    many = @Many(select = "com.itheima.dao.CheckItemDao.findCheckItemById"))
    })
    List<CheckGroup> findCheckGroupById(Integer id);
}
