package com.yjx.util;

import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.yjx.book.IDouBan;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DouBanUtil {
	
	public JSONObject getBouBanBookInfo(String isbn) throws IOException
	{
		return getDouBan(getUrl(isbn));
	}
	
	//获得豆瓣图书信息
	private static JSONObject getDouBan(String url) throws IOException
	{
		Connection con = Jsoup.connect(url);
        con.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        Response rs = con.execute(); 
        Document d1 = Jsoup.parse(rs.body());
        
        JSONObject json=new JSONObject();
        JSONObject json1=new JSONObject();
        JSONArray array1=new JSONArray();
        JSONArray array2=new JSONArray();
        
       json.put("bookName", d1.select("#wrapper").get(0).select("h1").select("span").get(0).ownText());
       json.put("image", d1.select("#wrapper").get(0).select("#mainpic").get(0).select(".nbg").attr("href"));
		
		for(int i=0;i<d1.select("#wrapper").get(0).select("#info").get(0).select(".pl").size();i++)
		{
        	array1.add(i, d1.select("#wrapper").get(0).select("#info").get(0).select(".pl").get(i).ownText());
        	String s=d1.select("#wrapper").get(0).select("#info").get(0).text();
        	if(i+2<=d1.select("#wrapper").get(0).select("#info").get(0).select(".pl").size())
        	{
        		array2.add(i, s.substring(s.indexOf(d1.select("#wrapper").get(0).select("#info").get(0).select(".pl").get(i).ownText())+4, s.indexOf(d1.select("#wrapper").get(0).select("#info").get(0).select(".pl").get(i+1).ownText())-1));
        	}
        	else
        	{
        		array2.add(i, s.substring(s.indexOf("ISBN:")+6));
        	}
		}
		json1.put("name", array1);
		json1.put("value", array2);
		json.put("info", json1);
     
	if(d1.select("#wrapper").get(0).select(".related_info").size()>0)
	{
		if(d1.select("#wrapper").get(0).select(".related_info").get(0).select("#link-report").size()>0)
		{
			if(d1.select("#wrapper").get(0).select(".related_info").get(0).select("#link-report").get(0).select(".intro").size()>0)
		       {
		    	   if(d1.select("#wrapper").get(0).select(".related_info").get(0).select("#link-report").get(0).select(".intro").size()>=2)
		    	   {
		    		   json.put("contentValidity", d1.select("#wrapper").get(0).select(".related_info").get(0).select("#link-report").get(0).select(".intro").get(1).text());
		    	   }
		    	   else
		        	{
		        		json.put("contentValidity", d1.select("#wrapper").get(0).select(".related_info").get(0).select("#link-report").get(0).select(".intro").get(0).text());
		        	}
		       }
		       else
		       {
		    	   json.put("contentValidity","");
		       }
		}
		else
		{
			json.put("contentValidity","");
		}
		
		if(d1.select("#wrapper").get(0).select(".related_info").get(0).select(".indent").size()>=2)    
		{
			 if(d1.select("#wrapper").get(0).select(".related_info").get(0).select(".indent").get(1).select(".intro").size()>0)
		        {
		        	json.put("AuthorBriefIntroduction", d1.select("#wrapper").get(0).select(".related_info").get(0).select(".indent").get(1).select(".intro").get(0).text());
		        }
		        else
		        {
		        	json.put("AuthorBriefIntroduction", "");
		        } 
		}
		else
		{
			json.put("AuthorBriefIntroduction", "");
		}
        
	}
	else
	{
		json.put("contentValidity","");
		json.put("AuthorBriefIntroduction", "");
		
	}
        return json;
	}
	
	//获得url
	private static String getUrl(String isbn)
	{
		ScriptEngineManager manager = new ScriptEngineManager(); 
		//获取一个指定的名称的脚本引擎 
		ScriptEngine engine = manager.getEngineByName("js"); 
		String s=null;
		try {
			String path =DouBanUtil.class.getClassLoader().getResource("./").getPath();;
			//String path="src/main/resources/";
			// FileReader的参数为所要执行的js文件的路径 
			engine.eval(new FileReader(path + "1.js"));
			if (engine instanceof Invocable) 
			{ 
				Invocable invocable = (Invocable) engine; //从脚本引擎中返回一个给定接口的实现 
				IDouBan executeMethod = invocable.getInterface(IDouBan.class); 
				//执行指定的js方法
				s = executeMethod.geturl(getDataKey(isbn));
			} 
			} 
		catch (Exception e)
		{
			e.printStackTrace(); 
			
		}
		return s;
	}
	
	//获得加密秘钥
	private static String getDataKey(String isbn) throws IOException
	{
		String url="https://book.douban.com/subject_search?search_text="+isbn+"&cat=1001";
		Connection con = Jsoup.connect(url);
        con.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        Response rs = con.execute(); 
        Document d1 = Jsoup.parse(rs.body());

        String s=d1.select("script").get(6).data();
      
       return s.substring(s.indexOf("window.__DATA__ = \"")+19, s.indexOf(";")-1);
	}


}
