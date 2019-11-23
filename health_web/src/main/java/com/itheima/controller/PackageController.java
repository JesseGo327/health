package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.utils.QiniuUtils;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * 套餐管理
 *
 * @Author: tangx
 * @Date: 2019/11/11 14:49
 * @Description: com.itheima.controller
 */
@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 图片上传
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam(" ") MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //获取文件后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            //使用UUID随机生成文件名称,防止文件名覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //图片上传成功,将图片名称存入Redis,基于Redis的set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    @PreAuthorize("hasAnyAuthority('SETMEAL_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            //调用业务
            PageResult pageResult = packageService.findPage(queryPageBean);
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('SETMEAL_ADD')")
    public Result add(@RequestBody Package pack, Integer[] checkgroupIds){
        try {
            packageService.add(pack,checkgroupIds);
        } catch(Exception e) {
            //新增套餐失败
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }
}
