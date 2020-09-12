package com.yjx.changdaplus.mapper.book;

import com.yjx.changdaplus.pojo.book.BookInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-02 14:42
 **/
@Repository
@Mapper
public interface IBookMapper {

    @Insert("insert into bookinfo(`bookName`, `isbn`, `no`, `info`) values(#{bookName},#{isbn},#{no},#{info})")
    int addBookInfo (BookInfo bookInfo);

    @Update("update bookinfo set info=#{info} where id=#{id}")
    int updateBookInfo (BookInfo bookInfo);

    @Select("select *from bookinfo where no=#{no}")
    BookInfo findBookInfo (String no);

}
