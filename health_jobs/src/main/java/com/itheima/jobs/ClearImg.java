package com.itheima.jobs;

import com.itheima.utils.QiniuUtils;
import com.itheima.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Author: tangx
 * @Date: 2019/11/11 19:21
 * @Description: com.itheima.jobs
 */
public class ClearImg {
    @Autowired
    private JedisPool jedisPool;

    //清理图片
    public void clearImg(){
        System.out.println("clearImg()...");

        //1计算set的值
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //2遍历集合
        for (String s : set) {
            //删除
            //删除七牛空间的
            QiniuUtils.deleteFileFromQiniu(s);
            //删除redis集合的
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
        }
        //删除两个key
        jedisPool.getResource().del(RedisConstant.SETMEAL_PIC_DB_RESOURCES,RedisConstant.SETMEAL_PIC_RESOURCES);
    }
}
