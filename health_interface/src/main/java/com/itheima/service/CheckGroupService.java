package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/10 16:12
 * @Description: com.itheima.service
 */
public interface CheckGroupService {

    void add(CheckGroup checkGroup, Integer[] checkItemIds);

    PageResult pageQuery(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(Integer[] checkitemIds, CheckGroup checkGroup);

    void deleteById(Integer id);

    List<CheckGroup> findAll();
}
