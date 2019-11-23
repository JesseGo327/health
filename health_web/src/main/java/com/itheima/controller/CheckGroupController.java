package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.interfaces.PBEKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/10 16:10
 * @Description: com.itheima.controller
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组
     * @param checkItemIds
     * @param checkGroup
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_ADD')")
    public Result addCheckGroup(@RequestParam Integer[] checkItemIds, @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.add(checkGroup,checkItemIds);
        } catch(Exception e) {
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.pageQuery(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }

    /**
     * 根据检查组id查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findById(Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        if (checkGroup!=null){
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }else {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


    /**
     * 根据检查组合id查询对应的所有检查项id
     * @param id
     * @return
     */
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
    }

    /**
     * 根据id修改
     * @param checkitemIds
     * @param checkGroup
     * @return
     */
    @RequestMapping("/edit")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_EDIT')")
    public Result edit(Integer[] checkitemIds,@RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.edit(checkitemIds,checkGroup);
        } catch(Exception e) {
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }


    /**
     * 根据id删除检查组
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_DELETE')")
    public Result deleteById(Integer id){
        try {
            checkGroupService.deleteById(id);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch(Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findAll")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findAll(){
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        if (checkGroupList !=null && checkGroupList.size()>0){
            Result result = new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
            return result;
        }else {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
