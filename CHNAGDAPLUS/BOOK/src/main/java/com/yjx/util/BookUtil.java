package com.yjx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BookUtil {
	
	//按书名查找图书
	public JSONObject getBook(String bookName, String page) throws Exception {
		// System.out.println(bookName+":"+page);
		String Url = "http://calis.yangtzeu.edu.cn:8000/opac/search?&q=" + bookName
				+ "&searchType=standard&isFacet=true&view=standard&searchWay=title&rows=10&sortWay=score&sortOrder=desc&searchWay0=marc&logical0=AND&page="
				+ page;
		Connection con = Jsoup.connect(Url);// 获取连接
		con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
		Response rs = con.execute();// 获取响应

		Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
		List<Element> et1 = d1.select(".resultTable");

		JSONObject json1 = new JSONObject();
		JSONObject json = new JSONObject();
		String regEx = "[^0-9.]";
		Pattern p = Pattern.compile(regEx);
		
		if (d1.select(".resultTable").attr("class").equals("resultTable")) {
			for (int i = 0; i < et1.get(0).select(".bookmetaTD").size(); i++) {
				JSONArray array=this.getCollectionInfo(et1.get(0).select(".bookmetaTD").get(i).select(".bookmeta").attr("bookrecno"));
				String s = et1.get(0).select(".bookmetaTD").get(i).select("div").get(3).text();
				Matcher m = p.matcher(s);
				JSONObject temp = new JSONObject()
						.element("isbn",
								et1.get(0).select(".bookmetaTD").get(i).select(".expressServiceTab")
										.attr("express_isbn"))
						.element("no", et1.get(0).select(".bookmetaTD").get(i).select(".bookmeta").attr("bookrecno"))
						.element("bookName",
								et1.get(0).select(".bookmetaTD").get(i).select("div").get(0).select(".bookmetaTitle")
										.get(0).select("a").get(0).ownText())
						.element("author",
								et1.get(0).select(".bookmetaTD").get(i).select("div").get(2).select("a").get(0)
										.ownText())
						.element("Press",
								et1.get(0).select(".bookmetaTD").get(i).select("div").get(3).select("a").get(0)
										.ownText())
						.element("PublicationDate", m.replaceAll("").trim()).element("Count",array.size());
				json.put(i + 1, temp);
			}
			String pag = d1.select(".meneame").get(0).select(".disabled").get(0).ownText();
			Matcher m1 = p.matcher(pag);
			json1.put("page", m1.replaceAll("").trim());
			json1.put("book", json);
		}
		return json1;
	}
	
	//获得书的详细信息
	public JSONObject getBookInfo(String no) throws IOException {
		String url = "http://calis.yangtzeu.edu.cn:8000/opac/book/" + no;
		Connection con = Jsoup.connect(url);// 获取连接
		con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
		Response rs = con.execute();// 获取响应

		Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
		List<Element> et1 = d1.select("#bookInfoTable");

		JSONObject json = new JSONObject();
		JSONArray temp = new JSONArray();
		JSONArray temp1 = new JSONArray();
		if (d1.select("#bookInfoTable").attr("id").equals("bookInfoTable")) {
			json.put("bookName", et1.get(0).select("tr").get(0).select("td").get(0).select("h2").get(0).ownText());
			for (int i = 1; i < et1.get(0).select("tr").size() - 2; i++) {
				temp.add(i - 1, et1.get(0).select("tr").get(i).select("td").get(0).select("div").get(0).ownText());
				temp1.add(i - 1, et1.get(0).select("tr").get(i).select("td").get(1).text());
			}
			json.element("name", temp).element("value", temp1).element("contentValidity", "")
					.element("AuthorBriefIntroduction", "");
		}
		return json;
	}

	//获得馆藏信息
	public JSONArray getCollectionInfo(String no) throws Exception
	{
		 String s;
		 String url="http://calis.yangtzeu.edu.cn:8000/opac/api/holding/"+no;
		 s=loadJson(url);
		 JSONObject json=new JSONObject().fromObject(s);
		 JSONArray a=new JSONArray();
		 JSONObject b=new JSONObject();
		 JSONObject c=new JSONObject();
		 JSONObject d=new JSONObject();
		 JSONObject e=new JSONObject();
		 a=(JSONArray) json.get("holdingList");
		 b=(JSONObject) json.get("holdStateMap");
		 c=(JSONObject) json.get("localMap");
		 d=(JSONObject) json.get("libcodeMap");
		 e=(JSONObject) json.get("pBCtypeMap");
		 JSONArray array=new JSONArray();
		 
		 for(int i=0;i<a.size();i++)
		 {
			 JSONObject temp=new JSONObject();
			 JSONObject temp1=new JSONObject();
			 temp=(JSONObject) a.get(i);
			 JSONObject t=(JSONObject) b.get(temp.get("state").toString());
			 JSONObject f=(JSONObject) e.get(temp.get("cirtype").toString());
			 if(!temp.get("cirtype").toString().equals("null"))
			 {
				 temp1.element("BarCode", temp.get("barcode")).element("CallNo",temp.get("callno")).
				 element("CollectionStatus", t.get("stateName")).element("CurLib", d.get(temp.get("curlib").toString())).
				 element("CurLocal", c.get(temp.get("curlocal"))).element("CirculationType",f.get("name") );
			 }
			 else
			 {

				 temp1.element("BarCode", temp.get("barcode")).element("CallNo",temp.get("callno")).
				 element("CollectionStatus", t.get("stateName")).element("CurLib", d.get(temp.get("curlib").toString())).
				 element("CurLocal", c.get(temp.get("curlocal"))).element("CirculationType","");
			 }
			 array.add(i, temp1);
		 }
		 return array;
	}

	private static String loadJson (String url) {  
        StringBuilder json = new StringBuilder();  
        try {  
            URL urlObject = new URL(url);  
            URLConnection uc = urlObject.openConnection();  
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));  
            String inputLine = null;  
            while ( (inputLine = in.readLine()) != null) {  
                json.append(inputLine);  
            }  
            in.close();  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return json.toString();  
    }


}
