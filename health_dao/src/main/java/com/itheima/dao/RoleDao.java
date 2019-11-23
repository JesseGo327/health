package com.itheima.dao;

import com.itheima.pojo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @Author: tangx
 * @Date: 2019/11/16 15:47
 * @Description: com.itheima.dao
 */
public interface RoleDao {
    /**
     * 根据uid查询角色
     * @param uid
     * @return
     */
    @Select("select * from t_role where id in(select role_id from t_user_role where user_id = #{uid})")
    Set<Role> findById(Integer uid);
}
