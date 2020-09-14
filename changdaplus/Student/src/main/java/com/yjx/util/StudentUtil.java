package com.yjx.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StudentUtil {
	private  Response  response=null;
	
	public Response getResponse() {
		return response;
	}
	
	//构造方法（传入学号以及密码,获得response对象）
	public StudentUtil(String no,String pwd)
	{
		int i=0;
		while(response==null)
		{
			i++;
			if(i==10)
			{
				break;
			}
			try {
				response=getCookies(no, pwd);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	//获得cookie
	private static Response getCookies(String no,String pwd) throws IOException
	{
		Connection con = Jsoup.connect("");	//连接到新教务处官网
        con.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        Response rs = con.execute();	//获得请求对象
		
        Document d1 = Jsoup.parse(rs.body());	//将获得到的html转换成dom树
        
        //获得盐值
        List<Element> et1 = d1.select("script");
        String salt=et1.get(4).data().substring(256,293);        
       
        HashUtil h=new HashUtil();
        pwd=salt+pwd;	
        pwd=h.hash(pwd, "SHA1");	//密码加盐，加密（此法不可逆转）
        
       List<Element> et2 = d1.select("#loginForm");	//获得登录的表单
        
       Map<String, String> datas = new HashMap<String, String>();
        for (Element e : et2.get(0).getAllElements()) {
            if (e.attr("name").equals("username")) {
                e.attr("value", no);	//用户名
            }
            if (e.attr("name").equals("password")) {
                e.attr("value", pwd);	//密码
            }
            if (e.attr("name").equals("encodedPassword")) {
                e.attr("value", pwd); 
            }
            if(e.attr("name").equals("session_locale"))
            {
            	e.attr("value","zh_CN");	//语言类型
            }
            if (e.attr("name").length() > 0) {
            	if (e.attr("name").equals("loginForm")||e.attr("name").equals("submitBtn")) {
                    continue;
                }
            	else
            	{
            		 datas.put(e.attr("name"), e.attr("value"));
            	}
            }
        }
        
        //发起第二次请求,带有登录表单信息和第一次登陆获得的cookies
        Connection con2 = Jsoup.connect("");
        con2.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        Response rs2=con2.cookies(rs.cookies()).data(datas).execute();
        //判断模拟登录是否成功（此判断是根据获得html的body片段长度来进行的）
        if(rs2.body().length()<rs.body().length())
        {
        	rs=null;
        }
        else
        {
        	//为了防止特殊情况，再次进行判断
        	if(rs2.body().length()==7390)
        	{
        		rs=null;
        	}
        }
        
      //返回的为什么是第一个Response对象？
      /*
       * 模拟登录是采用第一个Response对象的（cookies值+表单信息）来进行再次登录
       * 在第二次请求获得的Response对象的cookies值为空
       * 所以猜想，cookies是一直用第一个Response对象产生的
       */
        return rs;	
	}

	// 获得学生信息
	public JSONObject getStuInfo() throws IOException {
		// 本科生
		Document d2 = Jsoup.connect("").cookies(response.cookies())
				.get();
		JSONObject json = new JSONObject();
		if (d2.select(".infoTable").size() == 0) {
			// 研究生
			d2 = Jsoup.connect("")
					.cookies(response.cookies()).get();
		}
		if (d2.select(".infoTable").size() > 0) {

			json.element("stuName", d2.select(".infoTable").get(0).select("tr").get(1).select("td").get(3).ownText())
					.element("stuSex", d2.select(".infoTable").get(0).select("tr").get(2).select("td").get(3).ownText())
					.element("stuCampus",
			d2.select(".infoTable").get(0).select("tr").get(11).select("td").get(1).ownText());

		}
		return json;
	}

	// 获得成绩
	public JSONObject getScore() throws IOException {
		Document d1 = Jsoup.connect("").cookies(response.cookies()).get();
		List<Element> et = d1.select("table");
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray array = new JSONArray();
		JSONArray array1 = new JSONArray();
		if (et.size() == 2) {
			for (int i = 0; i < et.get(0).select("tr").size() - 2; i++) {
				JSONObject temp = new JSONObject();
				if (i == 0) {
					temp.element("SchoolYear", et.get(0).select("tr").get(0).select("th").get(0).ownText())
							.element("Semester", et.get(0).select("tr").get(0).select("th").get(1).ownText())
							.element("RequiredNumber", et.get(0).select("tr").get(0).select("th").get(2).ownText())
							.element("TotalCredit", et.get(0).select("tr").get(0).select("th").get(3).ownText())
							.element("AverageScorePoint", et.get(0).select("tr").get(0).select("th").get(4).ownText());
				} else {
					temp.element("SchoolYear", et.get(0).select("tr").get(i).select("td").get(0).ownText())
							.element("Semester", et.get(0).select("tr").get(i).select("td").get(1).ownText())
							.element("RequiredNumber", et.get(0).select("tr").get(i).select("td").get(2).ownText())
							.element("TotalCredit", et.get(0).select("tr").get(i).select("td").get(3).ownText())
							.element("AverageScorePoint", et.get(0).select("tr").get(i).select("td").get(4).ownText());
				}
				array.add(i, temp);
			}

			for (int i = 0; i < et.get(1).select("tr").size(); i++) {
				JSONObject temp = new JSONObject();
				if (et.get(1).select("tr").get(0).select("th").size() == 9) {
					if (i == 0) {
						temp.element("AcademicYear", et.get(1).select("tr").get(0).select("th").get(0).ownText())
								.element("CourseCode", et.get(1).select("tr").get(0).select("th").get(1).ownText())
								.element("CourseNumber", et.get(1).select("tr").get(0).select("th").get(2).ownText())
								.element("CourseTitle", et.get(1).select("tr").get(0).select("th").get(3).ownText())
								.element("CourseCategory", et.get(1).select("tr").get(0).select("th").get(4).ownText())
								.element("credit", et.get(1).select("tr").get(0).select("th").get(5).ownText())
								.element("score", et.get(1).select("tr").get(0).select("th").get(6).ownText())
								.element("FinalScore", et.get(1).select("tr").get(0).select("th").get(7).ownText())
								.element("AchievementPoint",
										et.get(1).select("tr").get(0).select("th").get(8).ownText());
					} else {
						temp.element("AcademicYear", et.get(1).select("tr").get(i).select("td").get(0).ownText())
								.element("CourseCode", et.get(1).select("tr").get(i).select("td").get(1).ownText())
								.element("CourseNumber", et.get(1).select("tr").get(i).select("td").get(2).ownText())
								.element("CourseTitle", et.get(1).select("tr").get(i).select("td").get(3).ownText())
								.element("CourseCategory", et.get(1).select("tr").get(i).select("td").get(4).ownText())
								.element("credit", et.get(1).select("tr").get(i).select("td").get(5).ownText())
								.element("score", et.get(1).select("tr").get(i).select("td").get(6).ownText())
								.element("FinalScore", et.get(1).select("tr").get(i).select("td").get(7).ownText())
								.element("AchievementPoint",
										et.get(1).select("tr").get(i).select("td").get(8).ownText());
					}
				} else {
					if (i == 0) {
						temp.element("AcademicYear", et.get(1).select("tr").get(0).select("th").get(0).ownText())
								.element("CourseCode", et.get(1).select("tr").get(0).select("th").get(1).ownText())
								.element("CourseNumber", et.get(1).select("tr").get(0).select("th").get(2).ownText())
								.element("CourseTitle", et.get(1).select("tr").get(0).select("th").get(3).ownText())
								.element("CourseCategory", et.get(1).select("tr").get(0).select("th").get(4).ownText())
								.element("credit", et.get(1).select("tr").get(0).select("th").get(5).ownText())
								.element("Make-upScore", et.get(1).select("tr").get(0).select("th").get(6).ownText())
								.element("score", et.get(1).select("tr").get(0).select("th").get(7).ownText())
								.element("FinalScore", et.get(1).select("tr").get(0).select("th").get(8).ownText())
								.element("AchievementPoint",
										et.get(1).select("tr").get(0).select("th").get(9).ownText());
					} else {
						temp.element("AcademicYear", et.get(1).select("tr").get(i).select("td").get(0).ownText())
								.element("CourseCode", et.get(1).select("tr").get(i).select("td").get(1).ownText())
								.element("CourseNumber", et.get(1).select("tr").get(i).select("td").get(2).ownText())
								.element("CourseTitle", et.get(1).select("tr").get(i).select("td").get(3).ownText())
								.element("CourseCategory", et.get(1).select("tr").get(i).select("td").get(4).ownText())
								.element("credit", et.get(1).select("tr").get(i).select("td").get(5).ownText())
								.element("Make-upScore", et.get(1).select("tr").get(i).select("td").get(6).ownText())
								.element("score", et.get(1).select("tr").get(i).select("td").get(7).ownText())
								.element("FinalScore", et.get(1).select("tr").get(i).select("td").get(8).ownText())
								.element("AchievementPoint",
										et.get(1).select("tr").get(i).select("td").get(9).ownText());
					}
				}
				array1.add(i, temp);
			}
			JSONObject temp1 = new JSONObject();
			temp1.element("SchoolSummary",
					et.get(0).select("tr").get(et.get(0).select("tr").size() - 2).select("th").get(0).ownText())
					.element("RequiredNumber",
							et.get(0).select("tr").get(et.get(0).select("tr").size() - 2).select("th").get(1)
									.ownText())
					.element("TotalCredit",
							et.get(0).select("tr").get(et.get(0).select("tr").size() - 2).select("th").get(2)
									.ownText())
					.element("AverageScorePoint", et.get(0).select("tr").get(et.get(0).select("tr").size() - 2)
							.select("th").get(3).ownText());
			json.element("Project", array);
			json.element("SchoolCollect", temp1);
			json.element("ScoreList", array1);
			json1.element("data", json);
		}
		return json1;
	}

	// 获得考试信息
	public JSONObject getExam() throws IOException {
		Document d1 = Jsoup.connect("").cookies(response.cookies()).get();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		JSONArray array1 = new JSONArray();
		
		//获得课程信息
		for (int i = 1; i < d1.select(".gridtable").get(0).select("tr").size(); i++) {
			JSONObject temp = new JSONObject();
			temp.element("CourseName", d1.select(".gridtable").get(0).select("tr").get(i).select("td").get(1).ownText())
			.element("CourseNumber",
			d1.select(".gridtable").get(0).select("tr").get(i).select("td").get(0).ownText())
			.element("CourseType",
			d1.select(".gridtable").get(0).select("tr").get(i).select("td").get(2).ownText())
			.element("Faculty",
			d1.select(".gridtable").get(0).select("tr").get(i).select("td").get(3).ownText())
			.element("Credit",
			d1.select(".gridtable").get(0).select("tr").get(i).select("td").get(4).ownText());
			array.add(temp);
		}
		
		//获得考试信息
		String url = ""
				+ d1.select("#semesterForm").get(0).select("#examBatchId").get(0).select("option").attr("value");
		try {
			Document d2 = Jsoup.connect(url).cookies(response.cookies()).get();
			
			if (d2.select("tbody").get(0).select("tr").size() > 1) {
				for (int i = 0; i < d2.select("tbody").get(0).select("tr").size(); i++) {
					JSONObject temp = new JSONObject();
					temp.element("ExamName", d2.select("tbody").get(0).select("tr").get(i).select("td").get(1).ownText())
					.element("ExamType",
					d2.select("tbody").get(0).select("tr").get(i).select("td").get(2).ownText())
					.element("ExamDate",
					d2.select("tbody").get(0).select("tr").get(i).select("td").get(3).ownText())
					.element("ExamSchedule",
					d2.select("tbody").get(0).select("tr").get(i).select("td").get(4).ownText())
					.element("ExamPlace",
					d2.select("tbody").get(0).select("tr").get(i).select("td").get(5).text())
					.element("ExamForm",
					d2.select("tbody").get(0).select("tr").get(i).select("td").get(6).ownText())
					.element("ExamSituation",
					d2.select("tbody").get(0).select("tr").get(i).select("td").get(7).ownText());
					if (temp.getString("ExamDate").equals("")) {
						temp.put("ExamDate", "时间未安排");
					}
					if (temp.getString("ExamSchedule").equals("")) {
						temp.put("ExamSchedule", "时间未安排");
					}
					if (temp.getString("ExamPlace").equals("")) {
						temp.put("ExamPlace", "地点未安排");
					}

					array1.add(temp);
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		json.put("Course", array);
		json.put("Exam", array1);

		return json;
	}
	
	// 获得ids
	public Map<String, String> getIds() throws IOException {
		Map<String, String> map = new HashMap<>();
		Document d1 = Jsoup.connect("")
				.cookies(response.cookies()).get();

		
		String ids	= d1.select("script").get(d1.select("script").size() - 1).data().substring(215, 220);
		
		String ids1 = d1.select("script").get(d1.select("script").size() - 1).data().substring(156, 162);
		Pattern pattern = Pattern.compile("[0-9]*");
		if (pattern.matcher(ids).matches()) {
			map.put("class", ids);
			if(pattern.matcher(ids1).matches())
			{
				map.put("std", ids1);
			}
			else
			{
				map.put("std", null);
			}
		} else{
			map.put("class", null);
			if(pattern.matcher(ids1).matches())
			{
				map.put("std", ids1);
			}
			else
			{
				map.put("std", null);
			}
		}
		return map;
	}

	// 获得课程信息
	public JSONArray getClassInfo(Map<String, String> map, String week) throws IOException {
		Document d1 = Jsoup.connect("")
				.cookies(response.cookies()).get();
		
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		List<Element> et1 = d1.select("#courseTableForm");
		// System.out.println(et1.size());
		if (et1.size() >= 1) {
			Map<String, String> datas = new HashMap<String, String>();
			for (Element e : et1.get(0).getAllElements()) {
				if (e.attr("name").equals("ignoreHead")) {
					e.attr("value", "1");
				}
				if (e.attr("name").equals("setting.kind")) {
					e.attr("value", "class");
				}
				if (e.attr("name").equals("startWeek")) {
					e.attr("value", week);
				}
				if (e.attr("name").equals("semester.id")) {
					//e.attr("value", "89");
				}

				if (e.attr("name").length() > 0) {

					datas.put(e.attr("name"), e.attr("value"));
				}
			}

			datas.put("ids", map.get(datas.get("setting.kind")));


			Document d4 = Jsoup.connect("")
					.cookies(response.cookies()).data(datas).get();
			
			String s = d4.select("script").get(d4.select("script").size() - 2).toString();
			String regex = "var teachers";
			String[] S = s.split(regex);

			for (int i = 1; i < S.length; i++) {
				JSONObject temp = new JSONObject();
				temp.put("teacher", getTeacher(S[i]).get("name"));
				temp.putAll(getClassName(getActivity(S[i])));
				temp.putAll(getIndex(getActivity(S[i])));
				array.add(temp);
			}
		}
		
		return array;
	}

	// 获得theacher信息
	private static JSONObject getTeacher(String s) {
		JSONArray array = new JSONArray().fromObject(s.substring(3, s.indexOf(";")));
		JSONObject json = new JSONObject().fromObject(array.get(0));
		// System.out.println(json.get("id"));
		// System.out.println(json.get("name"));
		return json;
	}

	// 获取Activity
	private static String getActivity(String s) {
		String[] a = s.split("activity =");
		// System.out.println(a[1]);
		return a[1];
	}

	// 获得课程名
	private static JSONObject getClassName(String s) {
		// JSONObject json=new JSONObject();
		JSONObject temp = new JSONObject();
		String[] a = s.split("index =");
		String[] b = a[0].split(",");
		temp.put("className", b[5].substring(1, b[5].length() - 1));
		if (b[7].equals("\"\"")) {
			temp.put("place", b[12].substring(1, b[12].length() - 1));
		} else {
			temp.put("place", b[7].substring(1, b[7].length() - 1));
		}
		// System.out.println(temp);
		return temp;
	}

	// 获得Index
	private static JSONObject getIndex(String s) {
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String[] a = s.split("index =");
		String x, y;	//获得“星期几”+“第几节课”
		for (int i = 1; i < a.length; i++) {
			JSONObject temp = new JSONObject();
			x = a[i].substring(0, 1);
			y = a[i].substring(a[i].indexOf("+") + 1, a[i].indexOf(";"));
			temp.element("week", x).element("section", y);
			array.add(temp);

		}
		json.put("index", array);
		return json;
	}
	
}
