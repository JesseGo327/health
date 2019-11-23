package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @Author: tangx
 * @Date: 2019/11/16 15:43
 * @Description: com.itheima.service.impl
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findUserByUserName(String username) {
        //根据用户名查询
        User user = userDao.findUserByUsername(username);
        if (user != null){
            //根据用户id查询角色
            Set<Role> roles = roleDao.findById(user.getId());

            //根据角色id查询角色
            if (roles!=null && roles.size()>0){
                for (Role role : roles) {
                    Integer roleId = role.getId();
                    Set<Permission> permissions = permissionDao.findById(roleId);
                    role.setPermissions(permissions);
                }
                user.setRoles(roles);
            }
        }
        return user;
    }
}
