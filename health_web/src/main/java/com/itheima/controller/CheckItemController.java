package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/7 17:36
 * @Description: ocm.itheima.controller
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    //新增
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.findPageByCodition(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }

    //删除数据
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    public Result deleteData(Integer id){
        try {
            checkItemService.deleteById(id);
        } catch(RuntimeException e) {
            return new Result(false,e.getMessage());
        } catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //查询单条数据
    @RequestMapping("/findById")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findCheckItemById(Integer id){
        //调用检查项服务,通过编号获取检查项信息
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    //编辑
    @RequestMapping("/updateById")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_EDIT')")
    public Result updateById(@RequestBody CheckItem checkItem){
        try {
            checkItemService.update(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //查询所有
    @RequestMapping("/findAll")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findAll(){
        List<CheckItem> list =  checkItemService.findAll();
        if (list != null && list.size() > 0){
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
            return result;
        }else {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
