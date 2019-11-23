package com.itheima.dao;

import com.itheima.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

/**
 * @Author: tangx
 * @Date: 2019/11/15 17:48
 * @Description: com.itheima.dao
 */
public interface MemberDao {

    /**
     * 判断当前是否是会员
     * @param telephone
     * @return
     */
    @Select(" select * from t_member where phoneNumber = #{phoneNumber}")
    Member findByTelephone(String telephone);

    /**
     * 增加会员
     * @param member
     */
    @Insert("insert into t_member " +
            "(fileNumber,name,sex,idCard,phoneNumber,regTime,password,email,birthday,remark) " +
            "values " +
            "(#{fileNumber},#{name},#{sex},#{idCard},#{phoneNumber},#{regTime},#{password},#{email},#{birthday},#{remark})")
    @SelectKey(statement = "select LAST_INSERT_ID()",keyProperty = "id",before = false,resultType = Integer.class)
    void add(Member member);

    @Select("select count(id) from t_member where regTime <= #{value}")
    Integer findMemberCountBeforeDate(String date);

    //今日新增会员数
    @Select("select count(id) from t_member where regTime = #{value}")
    Integer findMemberCountByDate(String today);

    //总会员数
    @Select("select count(id) from t_member")
    Integer findMemberTotalCount();

    //本月新增会员
    @Select("select count(*) from t_member where regTime >= #{value}")
    Integer findMemberCountAfterDate(String firstDayOfThisMonth);
}
