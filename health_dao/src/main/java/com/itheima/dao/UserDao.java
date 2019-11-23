package com.itheima.dao;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: tangx
 * @Date: 2019/11/16 15:43
 * @Description: com.itheima.dao
 */
public interface UserDao {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("SELECT * FROM t_user WHERE username = #{username}")
    User findUserByUsername(String username);
}
