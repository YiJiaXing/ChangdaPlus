package com.yjx.student.impl;

import java.io.IOException;

import com.yjx.student.IStuScore;
import com.yjx.util.StudentUtil;

import net.sf.json.JSONObject;

public class StuScoreImpl implements IStuScore {

	public JSONObject getStuScore(String no, String pwd) {
		StudentUtil su=new StudentUtil(no, pwd);
		JSONObject json=new JSONObject();
		
		if(su.getResponse()!=null)
		{
			try {
				json=su.getScore();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}

}
