package com.yjx.changdaplus.mapper.book;

import com.yjx.changdaplus.pojo.book.QueryBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-02 14:43
 **/
@Repository
@Mapper
public interface IQueryBookMapper {

    @Insert("insert into querybook (`bookNames`,`nos`,`wxId`) values(#{bookNames},#{nos},#{wxId})")
    int addQueryBook (QueryBook queryBook);

    @Update("update querybook set bookNames=#{bookNames} , nos=#{nos} where wxId = #{wxId}")
    int updateQueryBook (QueryBook queryBook);

    @Select("select *from querybook where wxId=#{wxId}")
    QueryBook findQueryBook (Integer wxId);
}
