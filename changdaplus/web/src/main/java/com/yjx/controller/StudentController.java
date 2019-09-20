package com.yjx.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yjx.pojo.UserInfo;
import com.yjx.service.IScoreService;
import com.yjx.service.IUserInfoService;
import com.yjx.student.IStuClass;
import com.yjx.student.IStuExam;
import com.yjx.student.IStuInfo;
import com.yjx.student.IStuScore;
import com.yjx.student.impl.StuClassImpl;
import com.yjx.student.impl.StuExamImpl;
import com.yjx.student.impl.StuInfoImpl;
import com.yjx.student.impl.StuScoreImpl;
import com.yjx.util.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
//@RequestMapping("/Loginweb")
public class StudentController {
	
	@Autowired
	IUserInfoService userInfoService;
	
	@Autowired
	IScoreService scoreService;
	
	//登录
	@RequestMapping(value="/Login",method=RequestMethod.GET)
	public @ResponseBody JSONObject Login(@RequestParam("code") String code)
	{
		String openid=new Util().getWeChatID(code).getString("openid");
		//String openid=code;
		UserInfo userInfo=new UserInfo();
		JSONObject json=new JSONObject();
		JSONObject json1=new JSONObject();
		if(openid!=null&&!openid.equals(""))
		{
			userInfo=userInfoService.getUserInfo(openid);
			int i=0;
			if(userInfo!=null)
			{
				i=1;
				if(userInfo.getNo()==null)
				{
					json.put("NoState","false");	//没绑定学号
				}
				else
				{
					json1.element("stuNo",userInfo.getNo()).element("stuName", userInfo.getName()).element("stuSex",userInfo.getSex()).element("stuCampus", userInfo.getCampus());
					json.put("StuInfo", json1);
					json.put("NoState", "true");	//绑定学号
				}
			}
			else
			{
				UserInfo userInfo1=new UserInfo();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				userInfo1.setOpenId(openid);
				userInfo1.setTime(df.format(new Date()));
				i=userInfoService.bind(userInfo1);	//绑定openid
				json.put("NoState","false");	//没绑定学号
			}
			if(i==1)
			{
				json.put("LState","true");
			}
			else
			{
				json.put("LState","false");
			}
		}
		else
		{
			json.put("LState","false");
        	json.put("NoState", "false");
		}
		return json;
	}

	//绑定学号
	@RequestMapping(value="/BoundStuNoServlet",method=RequestMethod.GET)
	public @ResponseBody JSONObject BoundStuNo(@RequestParam(value="code") String code,@RequestParam(value="userName") String UserName,@RequestParam(value="userpassword") String UserPassword)
	{
		JSONObject json=new JSONObject();
		IStuInfo stuInfo=new StuInfoImpl();	//学生信息
		UserInfo userInfo=new UserInfo();	//学生对象实例化
		JSONObject json1=new JSONObject();
		Util util=new Util();	//工具类
		String openid;
		if(code!=null&&!code.equals(""))
		{
			openid=util.getWeChatID(code).getString("openid");
			//openid=code;
		}
		else
		{
			openid="";
		}
		if(UserName!=null&&UserPassword!=null)
		{
			json=stuInfo.getStuInfo(UserName, UserPassword);
			if(!json.isEmpty())
			{
				String name=json.getString("stuName");
				String sex = json.getString("stuSex");
				String campus = json.getString("stuCampus");
				userInfo.setOpenId(openid);
				userInfo.setNo(UserName);
				userInfo.setPwd(util.AESEncode("changdaplus", UserPassword));
				userInfo.setName(name);
				userInfo.setSex(sex);
				userInfo.setCampus(campus);
				if(userInfoService.update(userInfo)==1)
				{
					//json1.put("StuInfo", json);
					json1.put("state", "true");
				}
				else
				{
					json1.put("state","false");
				}
			}
			else
			{
				json1.put("state","false");
			}
		}
		else
		{
			json1.put("state","false");
		}
		return json1;
	}

	//查询成绩
	@RequestMapping(value="/ScoreServlet",method=RequestMethod.GET)
	public @ResponseBody JSONObject Score(@RequestParam("code") String code)
	{
		Util util=new Util();
		String openid=new Util().getWeChatID(code).getString("openid");
		//String openid=code;
		UserInfo userInfo=userInfoService.getUserInfo(openid);
		JSONObject json=new JSONObject();
		JSONObject json1=new JSONObject();
		IStuScore stuScore=new StuScoreImpl();	//成绩接口
		
		if(userInfo.getNo()!=null)	//已经绑定学号
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			com.yjx.pojo.Score score=scoreService.getScore(userInfo.getNo());
			if(score!=null)		//数据库存有学生的成绩
			{
				json=new JSONObject().fromObject(score.getData());
				json.put("state", "true");
				
				//数据更新
				json1=stuScore.getStuScore(userInfo.getNo(),util.AESDncode("changdaplus", userInfo.getPwd()));
				if(!json1.isEmpty())
				{
					com.yjx.pojo.Score score1=new com.yjx.pojo.Score();
					score1.setNo(userInfo.getNo());
					score1.setData(json1.toString());
					score1.setTime(df.format(new Date()).toString());
					scoreService.updateScore(score1);	//更新到数据库
				}
				
			}
			else		//数据库没有学生的成绩
			{
				json=stuScore.getStuScore(userInfo.getNo(),util.AESDncode("changdaplus", userInfo.getPwd()));
				if(!json.isEmpty())
				{

					com.yjx.pojo.Score score1=new com.yjx.pojo.Score();
					score1.setNo(userInfo.getNo());
					score1.setData(json.toString());
					score1.setTime(df.format(new Date()).toString());
					scoreService.addScore(score1);	//存入数据库
					json.put("state", "true");
				}
				else
				{
					json.put("state", "false");
				}
			}
		}
		else
		{
			//未绑定学号
    		json.put("state", "false");
		}
		
		return json;
	}

	//更新成绩
	@RequestMapping(value="/RefreshScore",method=RequestMethod.GET)
	public @ResponseBody JSONObject RefreshScore(@RequestParam("code") String code)
	{
		Util util=new Util();
		String openid=new Util().getWeChatID(code).getString("openid");
		//String openid=code;
		UserInfo userInfo=userInfoService.getUserInfo(openid);
		JSONObject json=new JSONObject();
		IStuScore stuScore=new StuScoreImpl();	//成绩接口
		
		if(userInfo.getNo()!=null)	//已经绑定学号
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				json=stuScore.getStuScore(userInfo.getNo(),util.AESDncode("changdaplus", userInfo.getPwd()));
				if(!json.isEmpty())
				{

					com.yjx.pojo.Score score=new com.yjx.pojo.Score();
					score.setNo(userInfo.getNo());
					score.setData(json.toString());
					score.setTime(df.format(new Date()).toString());
					scoreService.updateScore(score);	//更新到数据库
					json.put("state", "true");
				}
				else
				{
					json.put("state", "false");
				}
		}
		else
		{
			//未绑定学号
    		json.put("state", "false");
		}
		
		return json;
	}

	//查询课表
	@RequestMapping(value="/ClassServlet",method=RequestMethod.GET)
	public @ResponseBody JSONArray Timetable(@RequestParam("code") String code,@RequestParam("week") String week)
	{
		Util util=new Util();
		JSONArray array=new JSONArray();
		UserInfo userInfo=new UserInfo();
		IStuClass stuClass=new StuClassImpl();
		
		if(code!=null&&!code.equals("")&&week!=null)
		{
			String openid=util.getWeChatID(code).getString("openid");
			//String openid=code;
			
	        if(openid!=null&&!openid.equals(""))
	        { 
	        	userInfo=userInfoService.getUserInfo(openid);
	        	if(userInfo.getNo()!=null&&userInfo.getPwd()!=null)
	        	{
	        		array=stuClass.getClass(userInfo.getNo(),util.AESDncode("changdaplus", userInfo.getPwd()) , week);
	        	}
	        }
		}
		return array;
	}

	//考试信息
	@RequestMapping(value="/GetExamServlet",method=RequestMethod.GET)
	public @ResponseBody JSONObject ExamInfo(@RequestParam("code") String code)
	{
		Util util=new Util();
		UserInfo userInfo=new UserInfo();
		JSONObject json=new JSONObject();
		IStuExam stuExam=new StuExamImpl();
		if(code!=null&&!code.equals(""))
		{
			String openid=util.getWeChatID(code).getString("openid");
			//String openid=code;
			if(openid!=null&&!openid.equals(""))
	        { 
	        	userInfo=userInfoService.getUserInfo(openid);
	        	if(userInfo.getNo()!=null&&userInfo.getPwd()!=null)
	        	{
	        		json=stuExam.getStuExam(userInfo.getNo(),util.AESDncode("changdaplus", userInfo.getPwd()));
	        		json.put("state", "true");
	        	}
	        	else
	        	{
	        		json.put("state", "false");
	        	}
	        }
			else
			{
				json.put("state", "false");
			}
		}
		return json;
	}
}
