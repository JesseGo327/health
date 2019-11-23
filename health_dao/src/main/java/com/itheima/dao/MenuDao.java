package com.itheima.dao;

import com.itheima.pojo.Menu;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/22 15:04
 * @Description: com.itheima.dao
 */
public interface MenuDao {

    //根据权限动态展示父菜单
    @Select("select " +
            "m.id,m.path,m.name title,m.icon " +
            "from " +
            "t_menu m,t_role_menu rm,t_role r,t_user u,t_user_role ur " +
            "where " +
            "m.`parentMenuId` IS NULL AND m.`id` = rm.`menu_id` AND rm.`role_id` = r.`id` AND ur.`user_id` = u.`id` AND ur.`role_id` = r.`id` AND u.`username`=#{username}")
    @Results({
            @Result(column = "id",property = "children",many = @Many(select = "com.itheima.dao.MenuDao.findChildrenByParentMenuId"))
    })
    List<Menu> findMenuByKeyword(String username);

    //根据权限动态展示子菜单
    @Select("SELECT id,path,NAME title,linkUrl FROM t_menu WHERE parentMenuId = #{id}")
    List<Menu> findChildrenByParentMenuId(Integer id);
}
