package com.yjx.changdaplus.rest;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.pojo.news.NewsDetail;
import com.yjx.changdaplus.pojo.news.NewsInfo;
import com.yjx.changdaplus.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @program: changda-plus
 * @description:新闻信息数据接口
 * @author: YJX
 * @create: 2020-08-30 21:12
 **/
@CrossOrigin
@RestController
@RequestMapping("/api/news")
public class NewsRest {

    @Autowired
    private INewsService newsService;

    /**
     * 1.获取学校新闻信息
     */
    @RequestMapping(value = "/getnews",method = RequestMethod.GET)
    public JSONObject getNews() {
        JSONObject dataJson ;
        NewsInfo newsInfo = newsService.findNewsInfo();
        if (newsInfo==null) {
            newsInfo = new NewsInfo();
            dataJson = newsService.getNews();
            if (dataJson.isEmpty()) {

            } else {
                newsInfo.setData(dataJson);
                newsInfo.setDate(new Date());
                newsService.addNewsInfo(newsInfo);
            }
        } else {
            dataJson = newsInfo.getData();
        }
        return dataJson;
    }

    /**
     * 2.新闻详细信息
     */
    @RequestMapping(value = "/newsinfo",method = RequestMethod.GET)
    public JSONObject getNewsInfo(@RequestParam Map<String, String> map) {
        JSONObject dataJson = new JSONObject(true);
        NewsDetail newsDetail = newsService.findNewsDetail(map.get("name"));
        if (newsDetail==null) {
            newsDetail = new NewsDetail();
            dataJson = newsService.getInfo(map.get("url"));
            if (dataJson.isEmpty()) {

            } else {
                newsDetail.setData(dataJson);
                newsDetail.setName(map.get("name"));
                newsService.addNewsDetail(newsDetail);
            }
        } else {
            dataJson = newsDetail.getData();
        }
        return dataJson;
    }

}
