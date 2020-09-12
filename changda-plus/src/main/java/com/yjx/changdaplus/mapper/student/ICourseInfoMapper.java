package com.yjx.changdaplus.mapper.student;

import com.yjx.changdaplus.pojo.student.CourseInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:课程Mapper
 * @author: YJX
 * @create: 2020-09-01 18:33
 **/
@Repository
@Mapper
public interface ICourseInfoMapper {
    @Insert("insert into courseinfo(`stuNo`,`course`) values (#{stuNo},#{course})")
    int addCourseInfo (CourseInfo courseInfo);

    @Update("update courseinfo set course=#{course} where id=#{id}")
    int updateCourseInfo (CourseInfo courseInfo);

    @Select("select *from courseinfo where stuNo=#{stuNo}")
    CourseInfo findCourseInfoByStuNo (String stuNo);
}
