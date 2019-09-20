package com.yjx.student;

import net.sf.json.JSONObject;

public interface IStuScore {
	
	public JSONObject getStuScore(String no, String pwd);	//获得成绩
}
