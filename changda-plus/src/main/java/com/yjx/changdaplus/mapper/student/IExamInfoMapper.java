package com.yjx.changdaplus.mapper.student;

import com.yjx.changdaplus.pojo.student.ExamInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:考试安排Mapper
 * @author: YJX
 * @create: 2020-09-01 18:34
 **/
@Repository
@Mapper
public interface IExamInfoMapper {
    @Insert("insert into examinfo(`stuNo`,`exam`) values (#{stuNo},#{exam})")
    int addExamInfo (ExamInfo examInfo);

    @Update("update examinfo set exam=#{exam} where id=#{id}")
    int updateExamInfo (ExamInfo examInfo);

    @Select("select *from examinfo where stuNo=#{stuNo}")
    ExamInfo findExamInfoByStuNo (String stuNo);
}
