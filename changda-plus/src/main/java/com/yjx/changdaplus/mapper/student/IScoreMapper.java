package com.yjx.changdaplus.mapper.student;

import com.yjx.changdaplus.pojo.student.Score;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:成绩Mapper
 * @author: YJX
 * @create: 2020-09-01 18:34
 **/
@Repository
@Mapper
public interface IScoreMapper {

    @Insert("insert into score(`stuNo`,`score`) values (#{stuNo},#{score})")
    int addScore (Score score);

    @Update("update score set score=#{score} where id=#{id}")
    int updateScore (Score score);

    @Select("select *from score where stuNo=#{stuNo}")
    Score findScoreByStuNo (String stuNo);

}
