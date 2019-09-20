/**
* Copyright © 2019 eSunny Info. Tech Ltd. All rights reserved.
* 
* @Package: com.yjx.util 
* @author: YJX
* @date: 2019年6月13日 下午1:51:46 
*/
package com.yjx.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/** 
 * @ClassName: WeatherUtil 
 * @Description: TODO
 * @author: YJX
 * @date: 2019年6月13日 下午1:51:46  
 */
public class WeatherUtil {
	
	public JSONObject getInfo(String cityid)
	{
		JSONObject json=getNow(cityid);
		JSONObject temp=getTmp(cityid);
		JSONObject temp1=getLife(cityid);
		json.getJSONObject("data").put("tem1", temp.getString("tem1"));
		json.getJSONObject("data").put("tem2", temp.getString("tem2"));
		json.getJSONObject("data").put("air_tips", temp1.getJSONArray("life").getJSONObject(0).getString("txt"));
        return json;
	}
	
	private static JSONObject getNow(String cityid)
	{
		   String url = "https://free-api.heweather.net/s6/weather/now?location=CN"+cityid+"&key=da4bbae30ac74e22b027097d658a8c59";
	       JSONObject json=new JSONObject();
	       JSONObject data=new JSONObject();
		   JSONObject temp=getJson(url);	//获得数据
		   JSONArray arrayTemp=temp.getJSONArray("HeWeather6");
		   JSONObject temp1=arrayTemp.getJSONObject(0);		//取首位
	       json.put("city", temp1.getJSONObject("basic").get("location"));	//城市
	       data.put("tem", temp1.getJSONObject("now").get("tmp"));
	       data.put("wea", temp1.getJSONObject("now").get("cond_txt"));
	       Integer cond_code=temp1.getJSONObject("now").getIntValue("cond_code");
	       if(cond_code==100)
	       {
	    	   data.put("wea_img", "qing");
	       }
	       else if(cond_code==101||cond_code==102||cond_code==103)
	       {
	    	   data.put("wea_img", "yun");
	       }
	       else if(cond_code==104)
	       {
	    	   data.put("wea_img", "yin");
	       }
	       else if(cond_code/100==3)
	       {
	    	   data.put("wea_img", "yu");
	       }
	       else if(cond_code/100==4)
	       {
	    	   data.put("wea_img", "xue");
	       }
	       json.put("data",data); 
	       return json;
	}
	
	private static JSONObject getTmp(String cityid)
	{
		String url = "https://free-api.heweather.net/s6/weather/forecast?location=CN"+cityid+"&key=da4bbae30ac74e22b027097d658a8c59";
		JSONObject json=new JSONObject();
		JSONObject temp=getJson(url);	//获得数据
		JSONArray arrayTemp=temp.getJSONArray("HeWeather6").getJSONObject(0).getJSONArray("daily_forecast");
		json.put("tem2", arrayTemp.getJSONObject(0).getString("tmp_min"));
		json.put("tem1", arrayTemp.getJSONObject(0).getString("tmp_max"));
		return json;
	}
	
	private static JSONObject getLife(String cityid)
	{
		String url = "https://free-api.heweather.net/s6/weather/lifestyle?location=CN"+cityid+"&key=da4bbae30ac74e22b027097d658a8c59";
		JSONObject json=new JSONObject();
		JSONObject temp=getJson(url);	//获得数据
	    json.put("life", temp.getJSONArray("HeWeather6").getJSONObject(0).getJSONArray("lifestyle"));
		return json;
		
	}
	
 	private static JSONObject getJson(String url)
	{
		HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        String json = null;
        JSONObject jsonObject=new JSONObject();
        try {
            // 通过HttpClient Get请求返回Json数据
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                json = EntityUtils.toString(entity, "UTF-8").trim();
                jsonObject = JSONObject.parseObject(json);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpGet.abort();
        }
        return jsonObject;
	}

}
