package com.itheima.dao;

import com.itheima.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @Author: tangx
 * @Date: 2019/11/16 15:47
 * @Description: com.itheima.dao
 */
public interface PermissionDao {
    /**
     * 根据角色Id查询权限
     * @param roleId
     * @return
     */
    @Select("SELECT * FROM t_permission WHERE id IN (SELECT permission_id FROM t_role_permission WHERE role_id = #{roleId})")
    Set<Permission> findById(Integer roleId);
}
