package com.yjx.message;

import net.sf.json.JSONObject;

public interface IMessage {

	public JSONObject getMessage();	//学校新闻
	
	public JSONObject getMessageInfo(String url);	//获得详细新闻信息
}
