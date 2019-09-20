package com.yjx.message;

import net.sf.json.JSONObject;

public interface IMessage {

	public JSONObject getMessage();//获得新闻
	
	public JSONObject getMessageInfo(String url);	//新闻详情
}
