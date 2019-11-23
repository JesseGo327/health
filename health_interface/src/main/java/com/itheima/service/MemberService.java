package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/16 14:48
 * @Description: com.itheima.service
 */
public interface MemberService {
    /**
     * 根据手机号查询会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 增加会员
     * @param member
     */
    void add(Member member);

    List<Integer> findMemberCountByMonth(ArrayList<String> list);
}
