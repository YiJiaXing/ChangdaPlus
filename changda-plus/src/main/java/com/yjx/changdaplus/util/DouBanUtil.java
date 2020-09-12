package com.yjx.changdaplus.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.service.IDouBan;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DouBanUtil {

	
	public JSONObject getBouBanBookInfo(String isbn) throws Exception
	{
		GetKey getKey =new GetKey(isbn);
		JaZai jaZai = new JaZai();

		try {
			new Thread(getKey).start();
			new Thread(jaZai).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (getKey.getPwdKey()==null) {
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return getDouBan(getUrl(getKey.getPwdKey(),jaZai.getFileReader()));
	}
	
	//获得豆瓣图书信息
	private static JSONObject getDouBan(String url) throws Exception
	{
		if (url==null) {
			throw new Exception("url为null");
		}
        Document d1 = Jsoup.connect(url).get();
        JSONObject json=new JSONObject();
        JSONObject json1=new JSONObject();
        JSONArray array1=new JSONArray();
        JSONArray array2=new JSONArray();

		Element element = d1.select("#wrapper").get(0);

       json.put("bookName", element.select("h1").select("span").get(0).ownText());
       json.put("image", element.select("#mainpic").get(0).select(".nbg").attr("href"));

		Elements elements = element.select("#info").get(0).select(".pl");
		for(int i=0;i<elements.size();i++)
		{
        	array1.add(i, elements.get(i).ownText());
        	String s=element.select("#info").get(0).text();
        	if(i+2<=elements.size())
        	{
        		array2.add(i, s.substring(s.indexOf(elements.get(i).ownText())+4, s.indexOf(elements.get(i+1).ownText())-1));
        	}
        	else
        	{
        		array2.add(i, s.substring(s.indexOf("ISBN:")+6));
        	}
		}
		json1.put("name", array1);
		json1.put("value", array2);
		json.put("info", json1);
     
	if(element.select(".related_info").size()>0)
	{
		Elements elements1 = element.select(".related_info").get(0).select("#link-report");
		if(elements1.size()>0)
		{
			if(elements1.get(0).select(".intro").size()>0)
		       {
		    	   if(elements1.get(0).select(".intro").size()>=2)
		    	   {
		    		   json.put("contentValidity", elements1.get(0).select(".intro").get(1).text());
		    	   }
		    	   else
		        	{
		        		json.put("contentValidity", elements1.get(0).select(".intro").get(0).text());
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
		
		if(element.select(".related_info").get(0).select(".indent").size()>=2)
		{
			Elements elements2 = element.select(".related_info").get(0).select(".indent");
			 if(elements2.get(1).select(".intro").size()>0)
		        {
		        	json.put("AuthorBriefIntroduction", elements2.get(1).select(".intro").get(0).text());
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
	private static String getUrl(String key,FileReader fileReader)
	{
//		long a= System.currentTimeMillis();
//		String path="src/main/resources/";
//		FileReader fileReader =null;
//		try {
//			fileReader = new FileReader(path + "1.js");
//			System.out.println("读取时间"+(System.currentTimeMillis()-a));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		ScriptEngineManager manager = new ScriptEngineManager(); 
		//获取一个指定的名称的脚本引擎 
		ScriptEngine engine = manager.getEngineByName("js"); 
		String s=null;
		try {
//			String path =DouBanUtil.class.getClassLoader().getResource("./").getPath();

			// FileReader的参数为所要执行的js文件的路径
			long q=System.currentTimeMillis();
			engine.eval(fileReader);
			if (engine instanceof Invocable) 
			{ 
				Invocable invocable = (Invocable) engine; //从脚本引擎中返回一个给定接口的实现
				IDouBan executeMethod = invocable.getInterface(IDouBan.class);
				//执行指定的js方法
//				s = executeMethod.geturl(getDataKey(isbn));
				s = executeMethod.geturl(key);
			} 
			} 
		catch (Exception e)
		{
			e.printStackTrace(); 
			
		}
		return s;
	}
	
//	//获得加密秘钥
//	private static String getDataKey(String isbn) throws IOException
//	{
//		String url="https://book.douban.com/subject_search?search_text="+isbn+"&cat=1001";
//        Document d1 = Jsoup.connect(url).get();
//        String s=d1.select("script").get(6).data();
//       return s.substring(s.indexOf("window.__DATA__ = \"")+19, s.indexOf(";")-1);
//	}

	class GetKey implements Runnable {
		private String pwdKey=null;
		private String isbn;

		public GetKey(String isbn) {
			this.isbn = isbn;
		}

		public String getPwdKey() {
			return pwdKey;
		}

		public GetKey() {
		}

		@Override
		public void run() {
			while (pwdKey==null) {
				try {
					pwdKey = getDataKey(isbn);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//获得加密秘钥
		private String getDataKey(String isbn) throws IOException
		{
			String url="https://book.douban.com/subject_search?search_text="+isbn+"&cat=1001";
			Document d1 = Jsoup.connect(url).get();
			String s=d1.select("script").get(6).data();
			return s.substring(s.indexOf("window.__DATA__ = \"")+19, s.indexOf(";")-1);
		}
	}


	class JaZai implements Runnable {

		private FileReader fileReader=null;

		public FileReader getFileReader() {
			return fileReader;
		}

		@Override
		public void run() {
//			String path="src/main/resources/";
			String path =DouBanUtil.class.getClassLoader().getResource("./").getPath();
			try {
				fileReader = new FileReader(path + "1.js");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}



