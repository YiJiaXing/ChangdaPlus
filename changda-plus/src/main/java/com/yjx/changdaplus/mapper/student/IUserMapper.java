package com.yjx.changdaplus.mapper.student;

import com.yjx.changdaplus.pojo.student.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:用户Mapper
 * @author: YJX
 * @create: 2020-09-01 18:16
 **/
@Repository
@Mapper
public interface IUserMapper {

    /**
     * 已经测试
     * @param user
     * @return
     */
    @Insert("insert into user(`stuNo`,`stuPwd`,`projectId`,`name`,`sex`,`campus`,`ids`,`semesterId`) values (#{stuNo},#{stuPwd},#{projectId},#{name},#{sex},#{campus},#{ids},#{semesterId})")
    int addUser (User user);

    @Update("update user set ids=#{ids},semesterId=#{semesterId},examBatchId=#{examBatchId} where id=#{id}")
    int updateUser (User user);

    @Select("select *from user where stuNo=#{stuNo}")
    User findUserByStuNo (String stuNo);

    @Select("select *from user where id=#{id}")
    User findUserById (Integer id);
}
