package com.yjx.message;

import net.sf.json.JSONObject;

public interface IMessage {

	public JSONObject getMessage();	//ѧУ����
	
	public JSONObject getMessageInfo(String url);	//�����ϸ������Ϣ
}
