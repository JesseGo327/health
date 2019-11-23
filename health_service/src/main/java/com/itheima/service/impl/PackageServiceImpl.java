package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.PackageDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/11 14:51
 * @Description: com.itheima.service.impl
 */
@Service(interfaceClass = PackageService.class)
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageDao packageDao;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Package> page = packageDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增套餐
     * @param pack
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void add(Package pack, Integer[] checkgroupIds) {
        //插入套餐
        packageDao.add(pack);
        //图片名称保存daoRedis
        savePic2Redis(pack.getImg());
        if (checkgroupIds!=null && checkgroupIds.length>0){
            //绑定套餐和检查组的多对多关系
            setPackageAndCheckGroup(pack.getId(),checkgroupIds);
        }
    }

    /**
     * 获取所有套餐信息
     * @return
     */
    @Override
    public List<Package> findAll() {
        return packageDao.findAll();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public Package findById(Integer id) {
        return packageDao.findById(id);
    }

    @Override
    public Map findPackageCount() {
        Map hashMap = new HashMap();
        ArrayList<String> arr = new ArrayList();
       List<Map<String,String>> list = packageDao.findPackageCount();
        for (Map<String, String> map : list) {
            arr.add(map.get("name"));
        }
        hashMap.put("packageNames",arr);
        hashMap.put("packageCount", list);
        return hashMap;
    }

    /**
     * 图片名称保存daoRedis
     * @param img
     */
    private void savePic2Redis(String img) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }

    //绑定套餐和检查组的多对多关系
    @Transactional
    public void setPackageAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("package_id",id);
            map.put("checkgroup_id",checkgroupId);
            packageDao.setPackageAndCheckGroup(map);
        }
    }
}
