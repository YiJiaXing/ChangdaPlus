package com.yjx.changdaplus.mapper.news;

import com.yjx.changdaplus.pojo.news.NewsInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-02 14:55
 **/
@Repository
@Mapper
public interface INewsInfoMapper {
    @Insert("insert into newsinfo (`data`,`date`) values(#{data},#{date})")
    int addNewsInfo (NewsInfo newsInfo);

    @Select("select id,data from newsinfo where date=date(now())")
    NewsInfo findNewsInfo ();
}
