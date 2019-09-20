package com.yjx.message.impl;

import java.io.IOException;

import com.yjx.message.IMessage;
import com.yjx.util.NewsUtil;

import net.sf.json.JSONObject;

public class MessageImpl implements IMessage {

	public JSONObject getMessage() {
		return new NewsUtil().getSchoolMessage();
	}

	public JSONObject getMessageInfo(String url) {
		JSONObject json=new JSONObject();
		try {
			json=new NewsUtil().getMessage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
