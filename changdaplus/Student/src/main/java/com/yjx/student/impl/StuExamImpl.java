package com.yjx.student.impl;

import java.io.IOException;

import com.yjx.student.IStuExam;
import com.yjx.util.StudentUtil;

import net.sf.json.JSONObject;

public class StuExamImpl implements IStuExam {

	public JSONObject getStuExam(String no, String pwd) {
		StudentUtil su=new StudentUtil(no, pwd);
		JSONObject json=new JSONObject();
		
		try {
			json=su.getExam();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
