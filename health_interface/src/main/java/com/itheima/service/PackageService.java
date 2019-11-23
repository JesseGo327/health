package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Package;

import java.util.List;
import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/11 14:50
 * @Description: com.itheima.service
 */
public interface PackageService {

    PageResult findPage(QueryPageBean queryPageBean);

    void add(Package pack, Integer[] checkgroupIds);

    List<Package> findAll();

    Package findById(Integer id);

    Map findPackageCount();
}
