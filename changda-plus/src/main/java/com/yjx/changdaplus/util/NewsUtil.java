package com.yjx.changdaplus.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @program: changda-plus
 * @description:新闻信息工具
 * @author: YJX
 * @create: 2020-08-30 21:15
 **/
public class NewsUtil {
    public JSONObject getNews (Page page) {
        JSONObject jsonObject = new JSONObject(true);
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONArray jsonArray3 = new JSONArray();
        JSONArray jsonArray4 = new JSONArray();
        JSONArray jsonArray5 = new JSONArray();

        //获取滚动新闻
        List<Selectable> listbd = page.getHtml().xpath("//div[@class='iban']/div[@class='bd']/ul/li").nodes();
        for (Selectable selectable:listbd) {
            JSONObject temp = new JSONObject(true);
            String imageUrl=page.getUrl().toString()+"/"+selectable.$("li","style").regex("\\(.*\\)").replace("\\(","").replace("\\)","").toString();
            temp.put("image",imageUrl);
            temp.put("url",selectable.$("a","href").toString());
            jsonArray1.add(temp);
        }
        //获得学校新闻
        List<Selectable> list1 = page.getHtml().xpath("//div[@class='inews w1002']/div[@class='picbox']/div[@class='hd']/ul/li").nodes();
        List<Selectable> list2 = page.getHtml().xpath("//div[@class='inews w1002']/div[@class='list']/ul/li").nodes();
        for (Selectable selectable : list1) {
            JSONObject temp = new JSONObject(true);
            String imageUrl = page.getUrl().toString()+ "/" + selectable.xpath("//div[@class='pic']").$("img","src").replace("\\\"","").toString();
            temp.put("title",selectable.xpath("//div[@class='txt']").$("a","title").replace("\\\"","").toString());
            temp.put("image",imageUrl);
            temp.put("url",selectable.xpath("//div[@class='txt']").$("a","href").toString());
            jsonArray2.add(temp);
        }

        for (Selectable selectable : list2) {
            JSONObject temp = new JSONObject(true);
            temp.put("title",selectable.$("a","title").replace("\\\"","").toString());
            temp.put("url",selectable.$("a","href").toString());
            jsonArray3.add(temp);
        }

        //通知公告
        List<Selectable> listg = page.getHtml().xpath("//div[@class='iac']/div[@class='w1002']/div[@class='list']").nodes();
        for (Selectable selectable : listg) {
            List<Selectable> listTemp = selectable.xpath("//ul/li").nodes();
            for (Selectable selectable1 : listTemp) {
                JSONObject temp = new JSONObject(true);
                temp.put("title",selectable1.$("a","title").replace("\\\"","").toString());
                temp.put("url",selectable1.$("a","href").toString());
                jsonArray4.add(temp);
            }
        }

        //学术动态
        List<Selectable> listxue1 = page.getHtml().xpath("//div[@class='inot w1002']/div[@class='fl']/dl").nodes();
        List<Selectable> listxue2 = page.getHtml().xpath("//div[@class='inot w1002']/div[@class='list']/ul/li").nodes();
        for (Selectable selectable : listxue1) {
            JSONObject temp = new JSONObject(true);
            temp.put("Date",selectable.xpath("//dt/strong/text()").toString());
            temp.put("time",selectable.xpath("//dt/span/text()").toString());
            temp.put("title",selectable.xpath("//dd/h4").$("a","title").replace("\\\"","").toString());
            temp.put("url",selectable.xpath("//dd/h4").$("a","href").toString());
            temp.put("rapporteur",selectable.xpath("//dd/p").regex("\\<[/]strong\\>.*\\<").replace("</strong>","").replace("<","").nodes().get(0).toString());
            temp.put("place",selectable.xpath("//dd/p").regex("\\<[/]strong\\>.*\\<").replace("</strong>","").replace("<","").nodes().get(1).toString());
            jsonArray5.add(temp);
        }
        for (Selectable selectable : listxue2) {
            JSONObject temp = new JSONObject(true);
            temp.put("Date",selectable.xpath("//span/text()").toString());
            temp.put("time","");
            temp.put("title",selectable.xpath("//span/a").$("a","title").replace("\\\"","").toString());
            temp.put("url",selectable.xpath("//span/a").$("a","href").toString());
            temp.put("rapporteur","");
            temp.put("place","");
            jsonArray5.add(temp);
        }
        jsonObject.put("rollnews",jsonArray1);
        jsonObject.put("schoolnews1",jsonArray2);
        jsonObject.put("schoolnews2",jsonArray3);
        jsonObject.put("noticenews",jsonArray4);
        jsonObject.put("sciencenews",jsonArray5);
        return jsonObject;
    }

    public JSONObject getJaoNews (Page page) {
        JSONObject jsonObject =new JSONObject(true);
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        List<Selectable> listjwtz = page.getHtml().xpath("//div[@id='jwtz']/div[@class='f_div']").nodes().get(0).xpath("//ul/li").nodes();
        List<Selectable> listjxdt = page.getHtml().xpath("//div[@id='jxdt']/div[@class='f_div']").nodes().get(0).xpath("//ul/li").nodes();
        for (Selectable selectable1 : listjwtz) {
            JSONObject temp = new JSONObject(true);
            temp.put("title",selectable1.$("a","title").replace("\\\"","").toString());
            temp.put("url",page.getUrl().toString()+"/"+selectable1.$("a","href").toString());
            jsonArray1.add(temp);
        }
        jsonArray1.remove(listjwtz.size()-1);
        for (Selectable selectable1 : listjxdt) {
            JSONObject temp = new JSONObject(true);
            temp.put("title",selectable1.$("a","title").replace("\\\"","").toString());
            temp.put("url",page.getUrl().toString()+"/"+selectable1.$("a","href").toString());
            jsonArray2.add(temp);
        }
        jsonArray2.remove(listjwtz.size()-1);
        jsonObject.put("jaowunotice",jsonArray1);
        jsonObject.put("jaowudynamic",jsonArray2);
        return jsonObject;
    }

    /**
     * 1.教务通知
     * @param page
     */
    public JSONObject getJMessage(Page page) {
        JSONObject jsonObject = new JSONObject();
        Selectable selectable = page.getHtml().xpath("//form[@name='_newscontent_fromname']");
        int size = selectable.$("h1").nodes().size();
        String string ="";
        if (size!=0) {
            string = selectable.$("h1").nodes().get(0).replace("<h1>","<h1 style=\"text-align:center\">").get();
        }

        Selectable selectable1 = selectable.xpath("//div[@id='arc_word']");
        List<Selectable> list = selectable1.$("img","src").nodes();
        for (Selectable selectable2 : list) {
//            selectable1 = selectable1.replace(selectable2.get(),page.getUrl().regex("http.*\\.cn").toString()+selectable2.get());
            selectable1 = selectable1.replace(selectable2.get(),selectable2.get());
        }
        jsonObject.put("text",string+"\n"+selectable1.get());
        return jsonObject;
    }

    /**
     * 2.新闻信息
     * @param page
     */
    public JSONObject getNMessage(Page page) {
        JSONObject jsonObject = new JSONObject();
        Selectable selectable = page.getHtml().xpath("//form[@name='_newscontent_fromname']//div[@class='v_news_content']");
        String title = page.getHtml().getDocument().title();
        List<Selectable> list = selectable.$("img","src").nodes();
        for (Selectable selectable1 : list) {
//            selectable = selectable.replace(selectable1.get(),page.getUrl().regex("http.*\\.cn").toString()+selectable1.get());
            selectable = selectable.replace(selectable1.get(),selectable1.get());
        }
        title="<h1 style=\"text-align:center\">"+title+"</h1>";
        jsonObject.put("text",title+"\n"+selectable.get());
        return jsonObject;
    }


}
