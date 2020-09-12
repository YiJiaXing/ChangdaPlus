package com.yjx.changdaplus.mapper.news;

import com.yjx.changdaplus.pojo.news.NewsDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-02 14:55
 **/
@Repository
@Mapper
public interface INewsDetailMapper {
    @Insert("insert into newsdetail (`name`,`data`) values(#{name},#{data})")
    int addNewsDetail (NewsDetail newsDetail);

    @Update("update newsdetail set data=#{data} where id= #{id}")
    int updateNewsDetail(NewsDetail newsDetail);

    @Select("select *from newsdetail where name=#{name}")
    NewsDetail findNewsDetail (String name);

}
