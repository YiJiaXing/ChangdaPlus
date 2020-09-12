package com.yjx.changdaplus.service;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.pojo.news.NewsDetail;
import com.yjx.changdaplus.pojo.news.NewsInfo;

/**
 * @program: changda-plus
 * @description:新闻
 * @author: YJX
 * @create: 2020-09-01 20:10
 **/
public interface INewsService {
    /**
     * 1. 新闻详细信息
     * @param newsDetail
     * @return
     */
    boolean addNewsDetail (NewsDetail newsDetail);
    boolean updateNewsDetail(NewsDetail newsDetail);
    NewsDetail findNewsDetail (String name);

    /**
     * 2. 新闻
     */
    boolean addNewsInfo (NewsInfo newsInfo);
    NewsInfo findNewsInfo ();

    JSONObject getNews ();
    JSONObject getInfo (String url);

}
