package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Author: tangx
 * @Date: 2019/11/15 16:41
 * @Description: com.itheima.controller
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送验证码短信
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        String validate = ValidateCodeUtils.generateValidateCode(4).toString();
        try {
            //发送验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validate );
        } catch(Exception e) {
            e.printStackTrace();
            //发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //发送成功,保存验证码到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,5 * 60,validate);
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch(Exception e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存在redis
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,5*60,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
