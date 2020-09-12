package com.yjx.changdaplus.processor;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.util.NewsUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @program: changda-plus
 * @description:新闻信息的获取
 * @author: YJX
 * @create: 2020-08-30 21:09
 **/
@Component
public class NewsProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setTimeOut(100).setSleepTime(200);
    private JSONObject dataJson;
    private JSONObject dataJson1;

    public JSONObject getDataJson() {
        return dataJson;
    }
    public JSONObject getDataJson1() {
        return dataJson1;
    }

    public void setDataJson(JSONObject dataJson) {
        this.dataJson = dataJson;
    }

    public void setDataJson1(JSONObject dataJson1) {
        this.dataJson1 = dataJson1;
    }

    @Override
    public void process(Page page) {
        NewsUtil newsUtil = new NewsUtil();
        if (page.getUrl().get().equals("http://www.yangtzeu.edu.cn")) {
            dataJson = newsUtil.getNews(page);
        } else if (page.getUrl().get().equals("http://jwc.yangtzeu.edu.cn")) {
            dataJson1 = newsUtil.getJaoNews(page);
        } else if (page.getUrl().get().contains("jwc")) {
            dataJson = newsUtil.getJMessage(page);
        } else {
            dataJson = newsUtil.getNMessage(page);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}
