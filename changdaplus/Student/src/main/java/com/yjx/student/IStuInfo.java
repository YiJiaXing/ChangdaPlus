package com.yjx.student;

import net.sf.json.JSONObject;

public interface IStuInfo {
	public JSONObject getStuInfo(String no,String pwd);	//获得学生基本信息
}
