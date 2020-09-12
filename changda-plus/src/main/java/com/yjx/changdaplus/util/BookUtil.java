package com.yjx.changdaplus.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.List;

/**
 * @program: changda-plus
 * @description:书籍信息工具
 * @author: YJX
 * @create: 2020-08-30 21:15
 **/
public class BookUtil {

    /**
     * 获取图书信息
     * @param page
     * @return
     */
    public JSONObject getBook(Page page) {
        JSONObject jsonObject = new JSONObject(true);
        JSONArray jsonArray = new JSONArray();
        String pageNo = page.getHtml().xpath("//div[@id='resultTile']/div[@class='meneame']/span[@class='disabled']").regex("[0-9].*[0-9]").replace(",","").get();
        List<Selectable> listr = page.getHtml().xpath("//div[@class='resultList']/table[@class='resultTable']/tbody/tr").nodes();
        String nos="";

        for (int i=0; i<listr.size(); i++) {

            if (i==listr.size()-1) {
                nos=nos+listr.get(i).xpath("//td[@class='coverTD']/a/img").$("img","bookrecno").toString();
            } else {
                nos=nos+listr.get(i).xpath("//td[@class='coverTD']/a/img").$("img","bookrecno").toString()+",";
            }
        }
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1 = getBookCount(nos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Selectable selectable : listr) {
            JSONObject temp = new JSONObject(true);
            temp.put("isbn",selectable.xpath("//td[@class='coverTD']/a/img").$("img","isbn").toString());
            temp.put("no",selectable.xpath("//td[@class='coverTD']/a/img").$("img","bookrecno").toString());
            temp.put("bookName",selectable.xpath("//td[@class='bookmetaTD']/div[@class='bookmeta']/div").nodes().get(0).xpath("//span[@class='bookmetaTitle']/a/text()").toString());
            temp.put("author",selectable.xpath("//td[@class='bookmetaTD']/div[@class='bookmeta']/div").nodes().get(1).xpath("//a/text()").toString());
            temp.put("Press",selectable.xpath("//td[@class='bookmetaTD']/div[@class='bookmeta']/div").nodes().get(2).xpath("//a/text()").replace(" ","").toString());
            temp.put("PublicationDate",selectable.xpath("//td[@class='bookmetaTD']/div[@class='bookmeta']/div").nodes().get(2).xpath("div/text()").regex("[0-9].*[0-9]").toString());
            temp.put("Count",jsonObject1.get(temp.getString("no")));
            jsonArray.add(temp);
        }
        jsonObject.put("page",pageNo==null?"0":pageNo);
        jsonObject.put("book",jsonArray);
        return jsonObject;
    }

    public JSONObject getBookInfo(String no) throws IOException {
        String url = "http://calis.yangtzeu.edu.cn:8000/opac/book/" + no;

        JSONObject json = new JSONObject();
        JSONArray temp = new JSONArray();
        JSONArray temp1 = new JSONArray();
        Document d1 = Jsoup.connect(url).get();
        List<Element> et1 = d1.select("#bookInfoTable");

        String isbn=d1.select("#leftDiv").get(0).select("div").get(1).select("#bookcover_img").attr("isbn");
        if (d1.select("#bookInfoTable").attr("id").equals("bookInfoTable")) {
            List<Element> elements = et1.get(0).select("tr");
            //isbn号
            json.put("isbn", isbn);
            //书名
            json.put("bookName", elements.get(0).select("td").get(0).select("h2").get(0).ownText());

            for (int i = 1; i < elements.size() - 2; i++) {
                temp.add(i - 1, elements.get(i).select("td").get(0).select("div").get(0).ownText());
                temp1.add(i - 1, elements.get(i).select("td").get(1).text());
            }

            //作者
            if(elements.size()>2) {
                String s=elements.get(3).select(".rightTD").get(0).text();
                json.put("author", s.substring(s.indexOf("/")+1));
            } else {
                json.put("author","");
            }

            json.put("name", temp);
            json.put("value", temp1);
            json.put("contentValidity", "");
            json.put("AuthorBriefIntroduction", "");
        }
        return json;
    }

    // 获得馆藏信息
    public JSONObject getCollectionInfo(Page page) {
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = JSONObject.parseObject(page.getJson().get());
        JSONArray  holdingList =  jsonObject.getJSONArray("holdingList");
        JSONObject holdStateMap = jsonObject.getJSONObject("holdStateMap");
        JSONObject localMap = jsonObject.getJSONObject("localMap");
        JSONObject libcodeMap = jsonObject.getJSONObject("libcodeMap");
        JSONObject pBCtypeMap = jsonObject.getJSONObject("pBCtypeMap");

        for (int i=0; i<holdingList.size(); i++) {
            JSONObject temp = holdingList.getJSONObject(i);
            JSONObject temp1 = new JSONObject(true);
            JSONObject p = holdStateMap.getJSONObject(temp.get("state").toString());
            JSONObject q = pBCtypeMap.getJSONObject(temp.get("cirtype").toString());
            if (!temp.getString("cirtype").equals("null")) {
                temp1.put("BarCode", temp.get("barcode"));
                temp1.put("CallNo", temp.get("callno"));
                temp1.put("CollectionStatus", p.get("stateName"));
                temp1.put("CurLib", libcodeMap.get(temp.get("curlib").toString()));
                temp1.put("CurLocal", localMap.get(temp.get("curlocal")));
                temp1.put("CirculationType", q.get("name"));
            } else {
                temp1.put("BarCode", temp.get("barcode"));
                temp1.put("CallNo", temp.get("callno"));
                temp1.put("CollectionStatus", p.get("stateName"));
                temp1.put("CurLib", libcodeMap.get(temp.get("curlib").toString()));
                temp1.put("CurLocal", localMap.get(temp.get("curlocal")));
                temp1.put("CirculationType", "");
            }
            jsonArray.add(temp1);
        }
        jsonObject1.put("collectionInfo",jsonArray);
        return jsonObject1;
    }

    public static JSONObject getBookCount(String nos) throws IOException {
        String url = "http://calis.yangtzeu.edu.cn:8000/opac/book/holdingPreviews?bookrecnos="+nos+"&curLibcodes=&return_fmt=json";
        String[] strings = nos.split(",");
        Document document = Jsoup.connect(url).ignoreContentType(true).get();
        JSONObject jsonObject = JSONObject.parseObject(document.body().ownText());
        JSONObject previews = jsonObject.getJSONObject("previews");
        JSONObject temp1 = new JSONObject(true);
        for (int i=0; i<strings.length; i++) {
            int count = 0;
            JSONArray tempArray = previews.getJSONArray(strings[i]);
            if (tempArray!=null) {
                for (int j=0; j<tempArray.size(); j++) {
                    JSONObject temp = tempArray.getJSONObject(j);
                    count = temp.getIntValue("copycount") + count;
                }
            }

            temp1.put(strings[i],count);
        }
        return temp1;
    }

}
