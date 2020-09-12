package com.yjx.changdaplus.mapper.student;

import com.yjx.changdaplus.pojo.student.ClassTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:课表Mapper
 * @author: YJX
 * @create: 2020-09-01 18:32
 **/
@Repository
@Mapper
public interface IClassTableMapper {

    @Insert("insert into  classtable(`stuNo`,`week`,`data`) values (#{stuNo},#{week},#{data})")
    int addClassTable (ClassTable classTable);

    @Update("update classtable set data=#{data} where id=#{id}")
    int updateClassTable (ClassTable classTable);

    @Select("select *from classtable where stuNo=#{stuNo} and week=#{week}")
    ClassTable findClassTableByStuNo (String stuNo,Integer week);
}
