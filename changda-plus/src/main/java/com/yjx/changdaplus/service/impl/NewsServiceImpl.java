package com.yjx.changdaplus.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.enum1.NewsUrl;
import com.yjx.changdaplus.mapper.news.INewsDetailMapper;
import com.yjx.changdaplus.mapper.news.INewsInfoMapper;
import com.yjx.changdaplus.pojo.news.NewsDetail;
import com.yjx.changdaplus.pojo.news.NewsInfo;
import com.yjx.changdaplus.processor.NewsProcessor;
import com.yjx.changdaplus.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-01 20:11
 **/
@Transactional
@Service
public class NewsServiceImpl implements INewsService {

    @Autowired
    private INewsInfoMapper newsInfoMapper;

    @Autowired
    private INewsDetailMapper newsDetailMapper;

    @Autowired
    private NewsProcessor newsProcessor;

    @Override
    public boolean addNewsDetail(NewsDetail newsDetail) {
        return newsDetailMapper.addNewsDetail(newsDetail) ==1;
    }

    @Override
    public boolean updateNewsDetail(NewsDetail newsDetail) {
        return newsDetailMapper.updateNewsDetail(newsDetail) ==1;
    }

    @Override
    public NewsDetail findNewsDetail(String name) {
        return newsDetailMapper.findNewsDetail(name);
    }

    @Override
    public boolean addNewsInfo(NewsInfo newsInfo) {
        return newsInfoMapper.addNewsInfo(newsInfo) ==1;
    }

    @Override
    public NewsInfo findNewsInfo() {
        return newsInfoMapper.findNewsInfo();
    }

    @Override
    public JSONObject getNews() {
        JSONObject dataJson = new JSONObject(true);
        /**
         * 学校新闻
         */
        Spider spider = Spider.create(newsProcessor).addUrl(NewsUrl.XUEXIAO.getUrl()).thread(12);
        spider.start();
        /**
         * 教务处新闻
         */
        Spider spider1 = Spider.create(newsProcessor).addUrl(NewsUrl.JIAOWU.getUrl()).thread(12);
        spider1.start();
        JSONObject jsonObject = newsProcessor.getDataJson();
        JSONObject jsonObject1 = newsProcessor.getDataJson1();
        while (jsonObject==null||jsonObject.isEmpty()||jsonObject1==null||jsonObject1.isEmpty()) {
            jsonObject1 = newsProcessor.getDataJson1();
            jsonObject = newsProcessor.getDataJson();
        }
        dataJson.put("xuexiao",jsonObject);
        dataJson.put("jiaowu",jsonObject1);
        return dataJson;
    }

    @Override
    public JSONObject getInfo(String url) {
        Spider spider = Spider.create(newsProcessor).addUrl(url).thread(12);
        spider.start();
        JSONObject jsonObject = newsProcessor.getDataJson();
        while (jsonObject==null||jsonObject.isEmpty()) {
            jsonObject = newsProcessor.getDataJson();
            if(jsonObject==null) {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }
}
