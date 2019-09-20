package com.yjx.student.impl;

import java.io.IOException;

import com.yjx.student.IStuInfo;
import com.yjx.util.StudentUtil;

import net.sf.json.JSONObject;

public class StuInfoImpl implements IStuInfo {

	//获得学生基本信息
	public JSONObject getStuInfo(String no, String pwd) {
		StudentUtil su=new StudentUtil(no, pwd);
		JSONObject json=new JSONObject();
		
		if(su.getResponse()!=null)
		{
			try {
				json=su.getStuInfo();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}

}
