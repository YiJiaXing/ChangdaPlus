package com.yjx.student.impl;

import java.io.IOException;
import java.util.Map;

import com.yjx.student.IStuClass;
import com.yjx.util.StudentUtil;

import net.sf.json.JSONArray;

public class StuClassImpl implements IStuClass {

	public JSONArray getClass(String no, String pwd, String week) {
		StudentUtil su=new StudentUtil(no, pwd);
		JSONArray array=new JSONArray();
		Map<String, String> map=null;
		int i = 0;
		while(map==null)
		{
			try {
				map=su.getIds();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(i==7)
			{
				break;
			}
			i++;
		}

		if(map!=null)
		{
			int j=0;
			StudentUtil su1=new StudentUtil(no, pwd);
			while(array.isEmpty())
			{
				try {
					array=su1.getClassInfo(map, week);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(j==10)
				{
					break;
				}
				j++;
			}
		}
		
		return array;
	}

}
