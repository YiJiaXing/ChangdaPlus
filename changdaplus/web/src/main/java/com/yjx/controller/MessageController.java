package com.yjx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yjx.message.IMessage;
import com.yjx.message.impl.MessageImpl;
import com.yjx.util.Util;
import com.yjx.util.WeatherUtil;

import net.sf.json.JSONObject;

@Controller
@ResponseBody
//@RequestMapping("/Loginweb")
public class MessageController {
	
	//获得新闻
	@RequestMapping(value="/GetMessageServlet",method=RequestMethod.GET)
	public JSONObject getMessage()
	{
		IMessage message=new MessageImpl();
		return message.getMessage();
		
	}
	
	//获得详细信息
	@RequestMapping(value="/ShowMessage",method=RequestMethod.GET)
	public JSONObject getMessageInfo(@RequestParam("url") String url)
	{
		IMessage message=new MessageImpl();
		return message.getMessageInfo(url);
	}
	
	//获得天气预报
	@RequestMapping(value="/WeatherServlet",method=RequestMethod.GET)
	public @ResponseBody com.alibaba.fastjson.JSONObject getWeather(@RequestParam("cityid") String cityid)
	{
		Util util=new Util();
		WeatherUtil weatherUtil=new WeatherUtil();
		com.alibaba.fastjson.JSONObject json=new com.alibaba.fastjson.JSONObject();
		try{
			json=util.getWeather(cityid);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if(json.isEmpty())
		{
			json=weatherUtil.getInfo(cityid);
		}
		//System.out.println(json);
		return json;
	}
	
}
