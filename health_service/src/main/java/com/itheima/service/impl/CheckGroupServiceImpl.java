package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/10 16:12
 * @Description: com.itheima.service.impl
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组
     * 添加检查组合,同事需要设置检查组合和检查项的关联关系
     * @param checkGroup
     * @param checkItemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(),checkItemIds);
    }

    /**
     * 查询当前页检查组
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        if (queryPageBean.getQueryString()!=null && queryPageBean.getQueryString().length()>0){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<CheckGroup> list = checkGroupDao.findPage(queryPageBean.getQueryString());
        PageInfo<CheckGroup> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组合id查询对应的所有检查项id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑检查组，同时需要更新和检查项的关联关系
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    @Transactional
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        //根据检查组id删除中间表数据(清理原有关联关系)
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系）
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
        //更新检查组基本信息
        checkGroupDao.edit(checkGroup);
    }

    /**
     * 删除检查组
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Integer id) {
        //删除检查组与检查项关联关系
        checkGroupDao.deleteAssociation(id);
        //删除检查组
        checkGroupDao.deleteById(id);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    //设置检查组合和检查项的关联关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if (checkitemIds!=null &&checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
