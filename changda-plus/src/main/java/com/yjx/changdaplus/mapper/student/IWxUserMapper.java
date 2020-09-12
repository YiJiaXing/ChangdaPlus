package com.yjx.changdaplus.mapper.student;

import com.yjx.changdaplus.pojo.student.WxUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:微信mapper
 * @author: YJX
 * @create: 2020-09-01 18:31
 **/
@Repository
@Mapper
public interface IWxUserMapper {
    /**
     * 已经测试
     * @param wxUser
     * @return
     */
    @Insert("insert into wxuser(`openId`) values(#{openId})")
    int addWxUser (WxUser wxUser);

    @Update("update wxuser set userId=#{userId} where id=#{id}")
    int updateWxUser (WxUser wxUser);

    @Select("select * from wxuser where openId=#{openId}")
    WxUser findWxUserByOpenId (String openId);

}
