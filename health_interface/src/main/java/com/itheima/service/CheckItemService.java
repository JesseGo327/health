package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/7 17:48
 * @Description: com.itheima.service
 */
public interface CheckItemService {

    void add(CheckItem checkItem);

    PageResult findPageByCodition(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();
}
