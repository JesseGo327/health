package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: tangx
 * @Date: 2019/11/15 17:24
 * @Description: com.itheima.service.impl
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;
    /**
     * 提交预约
     * @param map
     * @return
     */
    @Override
    @Transactional
    public Result order(Map map) throws Exception {
        //判断当前日期是否设置了预约
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting= orderSettingDao.findByOrderDate(date);
        if (orderSetting == null){
            //当天没有预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //判断当天预约人数是否已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number){
            //预约已满
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //判断当前是否是会员
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member !=null){
            //是会员,避免重复预约
            Integer memberId = member.getId();
            int packageId = Integer.parseInt((String) map.get("packageId"));
            Order order = new Order(memberId,date,null,null,packageId);
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size()>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //不是会员,需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);

        }
        // 进行预约,
        // 预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //保存预约信息到预约表
        Order order = new Order(member.getId(),
                date,
                (String)map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("packageId")));
        orderDao.add(order);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
