package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: tangx
 * @Date: 2019/11/16 15:33
 * @Description: com.itheima.service.impl
 */
@Component
public class springSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用业务,根据用户名查询
        User user = userService.findUserByUserName(username);
        if (user==null){
            return null;
        }

        //进行授权
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getKeyword()));
        }

        org.springframework.security.core.userdetails.User userDetails =new
                org.springframework.security.core.userdetails.User(username,user.getPassword(),grantedAuthorityList);
        //创建UserDetails对象返回


        return userDetails;
    }
}
