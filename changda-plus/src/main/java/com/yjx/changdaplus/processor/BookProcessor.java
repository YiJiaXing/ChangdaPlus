package com.yjx.changdaplus.processor;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.util.BookUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @program: changda-plus
 * @description:获取书籍信息
 * @author: YJX
 * @create: 2020-08-30 21:11
 **/
@Component
public class BookProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(10).setTimeOut(2000).setSleepTime(100);
    private JSONObject dataJson ;
    private JSONObject dataJson1 ;

    public JSONObject getDataJson() {
        return dataJson;
    }

    public JSONObject getDataJson1() {
        return dataJson1;
    }

    public void setDataJson1(JSONObject dataJson1) {
        this.dataJson1 = dataJson1;
    }

    public void setDataJson(JSONObject dataJson) {
        this.dataJson = dataJson;
    }

    @Override
    public void process(Page page) {
        BookUtil bookUtil = new BookUtil();
        if (page.getUrl().toString().contains("search")) {
            String danPage = page.getUrl().regex("page.*").regex("=.*").toString().substring(1);
            dataJson = bookUtil.getBook(page);
            dataJson.put("danPage",danPage);
        } else {
            dataJson1 = bookUtil.getCollectionInfo(page);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

}
