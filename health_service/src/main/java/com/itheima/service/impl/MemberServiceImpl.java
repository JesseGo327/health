package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/16 14:48
 * @Description: com.itheima.service.impl
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    /**
     * 根据手机号查询会员
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 增加会员
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(ArrayList<String> month) {
        List<Integer> list = new ArrayList<>();
        for (String m : month) {
            m = m.replaceAll("\\.", "-") + "-32";
            Integer count = memberDao.findMemberCountBeforeDate(m);
            list.add(count);
        }

        return list;
    }
}
