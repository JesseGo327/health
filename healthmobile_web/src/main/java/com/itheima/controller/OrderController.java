package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约
 * @Author: tangx
 * @Date: 2019/11/15 16:57
 * @Description: com.itheima.controller
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        //从redis缓存中取出手机号为telephone的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //取出前端传过来的验证码
        String validateCode = (String) map.get("validateCode");
        //校验
        if (codeInRedis == null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result =null;
        try {
            //调用体检预约服务
            map.put("orderType",Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch(Exception e) {
            e.printStackTrace();
            //预约失败
            return result;
        }
        if (result.isFlag()){
            //预约成功,发送短信通知
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone,orderDate);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) throws Exception {
        Map map = orderService.findById(id);
        if (map !=null){
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }else {
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
