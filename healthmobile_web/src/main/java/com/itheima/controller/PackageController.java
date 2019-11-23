package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import com.itheima.utils.QiniuUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/13 15:43
 * @Description: com.itheima.controller
 */
@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;

    /**
     * 查询所有套餐
     * @return
     */
    @RequestMapping("/getPackage")
    public Result getPackage(){
        try {
            List<Package> list = packageService.findAll();
            //设置图片的连接地址
            list.forEach(pack->{
                pack.setImg(QiniuUtils.domian+"/"+pack.getImg());
            });
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        } catch(Exception e) {
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Package pack = packageService.findById(id);
            pack.setImg(QiniuUtils.domian+"/"+pack.getImg());
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,pack);
        } catch(Exception e) {
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
}
