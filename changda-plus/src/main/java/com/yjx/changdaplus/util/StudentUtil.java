package com.yjx.changdaplus.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: changda-plus
 * @description:工具类
 * @author: YJX
 * @create: 2020-08-29 09:57
 **/
public class StudentUtil {

    Connection.Response response=null;

    private String no;
    private String pwd;

    public  StudentUtil() {

    }

    public StudentUtil(String no, String pwd) {
        this.no = no;
        this.pwd = pwd;
        int i=0;
        while(response==null)
        {
            i++;
            if(i==10)
            {
                break;
            }
            try {
                response=getCookie(no,pwd);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /**
     * 模拟登陆 获得cookie
     * @param no
     * @param pwd
     * @return
     * @throws IOException
     */
    public  static Connection.Response getCookie(String no,String pwd) throws IOException {
        /**
         * 1.获得盐值
         */
        Connection con = Jsoup.connect("http://221.233.24.23/eams/login.action");
        con.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        con.header("Connection","keep-alive");
        Connection.Response rs = con.execute();
        Document d1=rs.parse();
        String salt=d1.select("script").get(4).data().substring(256,293);

        /**
         * 2.密码加盐
         */
        pwd=HashUtil.hash(salt+pwd,"SHA1");

        /**
         * 3.登录参数
         */
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("username",no);
        datas.put("password",pwd);
        datas.put("encodedPassword",pwd);
        datas.put("session_locale","zh_CN");

        /**
         * 4.携带cookie与参数 再次请求
         */
        Connection.Response rs1=con.cookies(rs.cookies()).data(datas).execute();

        /**
         * 5.判断是否模拟登陆成功
         */
        if(rs1.body().length()<rs.body().length())
        {
            rs=null;
        }
        else
        {
            //为了防止特殊情况，再次进行判断
            if(rs1.body().length()==7390)
            {
                rs=null;
            }
        }
        return rs;
    }

    /**
     * 获得学生基本信息
     * @param page
     * @return
     */
    public JSONObject getStudentInfo(Page page) {
        JSONObject jsonObject=new JSONObject(true);
        if (page.getHtml().$(".infoTable").nodes().size()==0) {
            return jsonObject;
        }
        List<Selectable> list= page.getHtml().$(".infoTable").nodes().get(0).$("tr").nodes();
        jsonObject.put("stuName",list.get(1).$("td").nodes().get(3).xpath("//td/text()").toString());
        jsonObject.put("stuSex",list.get(2).$("td").nodes().get(3).xpath("//td/text()").toString());
        jsonObject.put("stuCampus",list.get(11).$("td").nodes().get(1).xpath("//td/text()").toString());
        return jsonObject;
    }

    /**
     * 获得成绩基本信息
     * @param page
     * @return
     */
    public JSONObject getScore(Page page) {
        JSONObject jsonObject1 = new JSONObject(true);
        JSONObject jsonObject2 = new JSONObject(true);
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        List<Selectable> listTable = page.getHtml().$("table").nodes();  //获得表格
        List<Selectable> listh1 = listTable.get(0).xpath("//thead[@class='gridhead']/tr/th").nodes();  //获得第一张表的表头
        List<Selectable> listr1 = listTable.get(0).xpath("//tbody/tr").nodes();  //获得第一张表的数据
        if (listh1.size()==0) {
            jsonObject1.put("data",jsonObject2);
            return jsonObject1;
        }
        JSONObject temp = new JSONObject(true);
        temp.put("SchoolYear",listh1.get(0).xpath("th/text()").toString());
        temp.put("Semester",listh1.get(1).xpath("th/text()").toString());
        temp.put("RequiredNumber",listh1.get(2).xpath("th/text()").toString());
        temp.put("TotalCredit",listh1.get(3).xpath("th/text()").toString());
        temp.put("AverageScorePoint",listh1.get(4).xpath("th/text()").toString());
        jsonArray1.add(0,temp);
        for (int i=0; i<listr1.size()-2; i++) {
            temp = new JSONObject(true);
            List<Selectable> listTemp=listr1.get(i).$("td").nodes();
            temp.put("SchoolYear",listTemp.get(0).xpath("td/text()").toString());
            temp.put("Semester",listTemp.get(1).xpath("td/text()").toString());
            temp.put("RequiredNumber",listTemp.get(2).xpath("td/text()").toString());
            temp.put("TotalCredit",listTemp.get(3).xpath("td/text()").toString());
            temp.put("AverageScorePoint",listTemp.get(4).xpath("td/text()").toString());
            jsonArray1.add(i+1,temp);
        }

        List<Selectable> listh2 = listTable.get(1).xpath("//thead[@class='gridhead']/tr/th").nodes();  //获得第二张表的表头
        List<Selectable> listr2 = listTable.get(1).xpath("//tbody/tr").nodes();  //获得第二张表的数据
        JSONObject temp1 = new JSONObject(true);
        if (listh2.size()==9) {
            temp1.put("AcademicYear", listh2.get(0).xpath("th/text()").toString());
            temp1.put("CourseCode", listh2.get(1).xpath("th/text()").toString());
            temp1.put("CourseNumber", listh2.get(2).xpath("th/text()").toString());
            temp1.put("CourseTitle", listh2.get(3).xpath("th/text()").toString());
            temp1.put("CourseCategory", listh2.get(4).xpath("th/text()").toString());
            temp1.put("credit", listh2.get(5).xpath("th/text()").toString());
            temp1.put("score", listh2.get(6).xpath("th/text()").toString());
            temp1.put("FinalScore", listh2.get(7).xpath("th/text()").toString());
            temp1.put("AchievementPoint",listh2.get(8).xpath("th/text()").toString());
            jsonArray2.add(0,temp1);
            for (int i=0; i<listr2.size(); i++) {
                temp1 = new JSONObject(true);
                List<Selectable> listTemp=listr2.get(i).$("td").nodes();
                temp1.put("AcademicYear", listTemp.get(0).xpath("td/text()").toString());
                temp1.put("CourseCode", listTemp.get(1).xpath("td/text()").toString());
                temp1.put("CourseNumber", listTemp.get(2).xpath("td/text()").toString());
                temp1.put("CourseTitle", listTemp.get(3).xpath("td/text()").toString());
                temp1.put("CourseCategory", listTemp.get(4).xpath("td/text()").toString());
                temp1.put("credit", listTemp.get(5).xpath("td/text()").toString());
                temp1.put("score", listTemp.get(6).xpath("td/text()").toString().replace(" ",""));
                temp1.put("FinalScore", listTemp.get(7).xpath("td/text()").toString().replace(" ",""));
                temp1.put("AchievementPoint",listTemp.get(8).xpath("td/text()").toString().replace(" ",""));
                jsonArray2.add(i+1,temp1);
            }
        } else {
            temp1.put("AcademicYear", listh2.get(0).xpath("th/text()").toString());
            temp1.put("CourseCode", listh2.get(1).xpath("th/text()").toString());
            temp1.put("CourseNumber", listh2.get(2).xpath("th/text()").toString());
            temp1.put("CourseTitle", listh2.get(3).xpath("th/text()").toString());
            temp1.put("CourseCategory", listh2.get(4).xpath("th/text()").toString());
            temp1.put("credit", listh2.get(5).xpath("th/text()").toString());
            temp1.put("Make-upScore", listh2.get(6).xpath("th/text()").toString());
            temp1.put("score", listh2.get(7).xpath("th/text()").toString());
            temp1.put("FinalScore", listh2.get(8).xpath("th/text()").toString());
            temp1.put("AchievementPoint",listh2.get(9).xpath("th/text()").toString());
            jsonArray2.add(0,temp1);
            for (int i=0; i<listr2.size(); i++) {
                temp1 = new JSONObject(true);
                List<Selectable> listTemp=listr2.get(i).$("td").nodes();
                temp1.put("AcademicYear", listTemp.get(0).xpath("td/text()").toString());
                temp1.put("CourseCode", listTemp.get(1).xpath("td/text()").toString());
                temp1.put("CourseNumber", listTemp.get(2).xpath("td/text()").toString());
                temp1.put("CourseTitle", listTemp.get(3).xpath("td/text()").toString());
                temp1.put("CourseCategory", listTemp.get(4).xpath("td/text()").toString());
                temp1.put("credit", listTemp.get(5).xpath("td/text()").toString());
                temp1.put("Make-upScore", listTemp.get(6).xpath("td/text()").toString());
                temp1.put("score", listTemp.get(7).xpath("td/text()").toString().replace(" ",""));
                temp1.put("FinalScore", listTemp.get(8).xpath("td/text()").toString().replace(" ",""));
                temp1.put("AchievementPoint",listTemp.get(9).xpath("td/text()").toString().replace(" ",""));
                jsonArray2.add(i+1,temp1);
            }
        }

        JSONObject temp2 = new JSONObject(true);
        temp2.put("SchoolSummary",listr1.get(listr1.size()-2).$("th").nodes().get(0).xpath("th/text()").toString());
        temp2.put("RequiredNumber",listr1.get(listr1.size()-2).$("th").nodes().get(1).xpath("th/text()").toString());
        temp2.put("TotalCredit",listr1.get(listr1.size()-2).$("th").nodes().get(2).xpath("th/text()").toString());
        temp2.put("AverageScorePoint",listr1.get(listr1.size()-2).$("th").nodes().get(3).xpath("th/text()").toString());
        jsonObject2.put("Project",jsonArray1);
        jsonObject2.put("ScoreList",jsonArray2);
        jsonObject2.put("SchoolCollect",temp2);
        jsonObject1.put("data",jsonObject2);
        return jsonObject1;
    }

    /**
     * 获取 semesterId
     * @param page
     * @return
     */
    public String getSemesterId (Page page) {
        String semesterId = page.getHtml().$("script").nodes().get(18).regex("if.*else").regex("value.*\\\"}").regex("\\\".*\\\"").replace("\\\"","").toString();
        return semesterId;
    }

    /**
     * 获取 当学期课程信息
     * @param page
     * @return
     */
    public JSONObject getCourseInfo (Page page) {
//        System.out.println(page.getHtml());
        JSONObject jsonObject = new JSONObject(true);
        JSONArray jsonArray = new JSONArray();
        String examBatchId=null;
        List<Selectable> list = page.getHtml().$("#examBatchId").$("option").nodes();

        if (list.size()!=0) {
            examBatchId=list.get(0).$("option","value").toString();
        }

        List<Selectable> listTable = page.getHtml().xpath("//table[@class='gridtable']").nodes();
        List<Selectable> listr = listTable.get(0).xpath("//tbody/tr").nodes();
        for (Selectable selectable:listr) {
            JSONObject temp = new JSONObject(true);
            temp.put("CourseNumber", selectable.$("td").nodes().get(0).xpath("//td/text()").toString());
            temp.put("CourseName", selectable.$("td").nodes().get(1).xpath("//td/text()").toString());
            temp.put("CourseType", selectable.$("td").nodes().get(2).xpath("//td/text()").toString());
            temp.put("Faculty", selectable.$("td").nodes().get(3).xpath("//td/text()").toString());
            temp.put("Credit", selectable.$("td").nodes().get(4).xpath("//td/text()").toString());
            jsonArray.add(temp);
        }
        jsonObject.put("Course",jsonArray);
        jsonObject.put("examBatchId",examBatchId);
        return jsonObject;
    }

    /**
     * 获取 当前学期的考试信息
     * @param page
     * @return
     */
    public JSONObject getExamInfo (Page page) {
        JSONObject jsonObject =new JSONObject(true);
        JSONArray jsonArray = new JSONArray();
        List<Selectable> listTable = page.getHtml().xpath("//table[@class='gridtable']").nodes();
        if (listTable.size()!=1) {
            return null;
        }
        List<Selectable> listTbody = listTable.get(0).xpath("//tbody").$("tr").nodes();
        for (Selectable selectable:listTbody) {
            JSONObject temp = new JSONObject(true);
            temp.put("ExamName", selectable.$("td").nodes().get(1).xpath("//td/text()").toString());
            temp.put("ExamType", selectable.$("td").nodes().get(2).xpath("//td/text()").toString());
            temp.put("ExamDate", selectable.$("td").nodes().get(3).xpath("//td/font/text()").toString());
            temp.put("ExamSchedule", selectable.$("td").nodes().get(4).xpath("//td/font/text()").toString());
            temp.put("ExamPlace", selectable.$("td").nodes().get(5).xpath("//td/font/text()").toString());
            temp.put("ExamSeatNo", selectable.$("td").nodes().get(6).xpath("//td/font/text()").toString());
            temp.put("ExamForm", selectable.$("td").nodes().get(7).xpath("//td/text()").toString());
            temp.put("ExamSituation", selectable.$("td").nodes().get(8).xpath("//td/text()").toString());
            jsonArray.add(temp);
        }
        jsonObject.put("Exam",jsonArray);
        return jsonObject;
    }

    /**
     * 获取 课表信息
     * @param week
     * @param map
     * @return
     * @throws IOException
     */
    public JSONArray getClassInfo (Map<String,String> cookies, String week,Map<String,String> map) throws IOException {
        JSONArray array = new JSONArray();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("ignoreHead","1");
        datas.put("setting.kind","std");
        datas.put("project.id","1");
        datas.put("startWeek",week);
        datas.put("semester.id",map.get("semesterId"));
        datas.put("ids",map.get("ids"));

        Document d1 = Jsoup.connect("http://jwc3.yangtzeu.edu.cn/eams/courseTableForStd!courseTable.action").cookies(cookies).data(datas).get();
        String s = d1.select("script").get(d1.select("script").size() - 2).toString();
        String regex = "var teachers";
        String[] S = s.split(regex);

        for (int i = 1; i < S.length; i++) {
            JSONObject temp = new JSONObject();
            temp.put("teacher", getTeacher(S[i]).get("name"));
            temp.putAll(getClassName(getActivity(S[i])));
            temp.putAll(getIndex(getActivity(S[i])));
            array.add(temp);
        }
        return array;
    }

    /**
     * 获得 ids
     * @param page
     * @return
     */
    public String getIds (Page page) {
        List<Selectable> list = page.getHtml().$("script").nodes();
        System.out.println(page.getHtml());
        return list.get(list.size()-1)
                .regex("if\\(jQuery.*\\}else\\{")
                .regex("\\\"ids\",.*\\)")
                .regex(",\\\".*\\\"")
                .replace(",","")
                .replace("\\\"","")
                .toString();
    }

    /**
     * 获得theacher信息
     * @param s
     * @return
     */
    private static JSONObject getTeacher(String s) {
        JSONArray array = JSONArray.parseArray(s.substring(3, s.indexOf(";")));
        JSONObject json = (JSONObject) array.get(0);
        return json;
    }

    /**
     * 获取Activity
     * @param s
     * @return
     */
    private static String getActivity(String s) {
        String[] a = s.split("activity =");
        return a[1];
    }

    /**
     * 获得课程名
     * @param s
     * @return
     */
    private static JSONObject getClassName(String s) {
        JSONObject temp = new JSONObject();
        String[] a = s.split("index =");
        String[] b = a[0].split(",");
        temp.put("className", b[5].substring(1, b[5].length() - 1));
        if (b[7].equals("\"\"")) {
            temp.put("place", b[12].substring(1, b[12].length() - 1));
        } else {
            temp.put("place", b[7].substring(1, b[7].length() - 1));
        }
        return temp;
    }

    /**
     * 获得Index
     * @param s
     * @return
     */
    private static JSONObject getIndex(String s) {
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        String[] a = s.split("index =");
        String x, y;	//获得“星期几”+“第几节课”
        for (int i = 1; i < a.length; i++) {
            JSONObject temp = new JSONObject(true);
            x = a[i].substring(0, 1);
            y = a[i].substring(a[i].indexOf("+") + 1, a[i].indexOf(";"));
            temp.put("week", x);
            temp.put("section", y);
            array.add(temp);

        }
        json.put("index", array);
        return json;
    }

    /**
     * 获得学生基本信息
     * @return
     */
    public JSONObject getStudentInfo() throws IOException {
        /**
         * 学生基本信息   json
         * true 按照链表的方式存数据  记录插入顺序
         */
        JSONObject jsonObject =new JSONObject(true);
        /**
         * 1.本科生
         *  http://jwc3.yangtzeu.edu.cn/eams/stdDetail!innerIndex.action?projectId=1
         */
        Document d1 = Jsoup.connect("http://jwc3.yangtzeu.edu.cn/eams/stdDetail!innerIndex.action?projectId=1").cookies(response.cookies()).get();

        /**
         * 2.若以本科生身份登录 没有获得基本信息 切换研究生身份
         */
        if (d1.select(".infoTable").size()==0) {
            /**
             * 3.研究生
             * http://jwc3.yangtzeu.edu.cn/eams/stdDetail!innerIndex.action?projectId=3
             */
            d1 = Jsoup.connect("http://jwc3.yangtzeu.edu.cn/eams/stdDetail!innerIndex.action?projectId=1").cookies(response.cookies()).get();
        }

        /**
         * 4.再次判断是否获得基本信息
         */
        if (d1.select(".infoTable").size()!=0) {
            /**
             * 5.取基本信息  姓名  性别  所在校区
             */
            Element element = d1.select(".infoTable").get(0);
            jsonObject.put("stuName",element.select("tr").get(1).select("td").get(3).ownText());
            jsonObject.put("stuSex",element.select("tr").get(2).select("td").get(3).ownText());
            jsonObject.put("stuCampus",element.select("tr").get(11).select("td").get(1).ownText());
        }
        return jsonObject;
    }

    /**
     * 获得成绩信息
     * @return
     * @throws IOException
     */
    public JSONObject getScore() throws IOException {

        /**
         * 1.获取成绩信息
         * http://221.233.24.23/eams/teach/grade/course/person!historyCourseGrade.action?projectType=MAJOR
         */
        Document d1 = Jsoup.connect("http://221.233.24.23/eams/teach/grade/course/person!historyCourseGrade.action?projectType=MAJOR").cookies(response.cookies()).get();
        List<Element> list = d1.select("table");
        /**
         * 2.判断是否获得成绩数据
         * array1  学年度 学期 必选修  学分  绩点  信息汇总
         * array1.add(i,temp);  将数据添加入array1的json数组
         * array2  学年学期 课程代码 课程序号 课程名称 课程类别 学分 总评成绩 最终 绩点 信息
         * array2.add(i,temp);  将数据添加入array2的json数组
         */
        JSONObject jsonObject1 = new JSONObject(true);
        JSONObject jsonObject2 = new JSONObject(true);
        JSONArray array1=new JSONArray();
        JSONArray array2=new JSONArray();
        if (list.size()==2) {
            /**
             * 3.第一个table  list.get(0) 获得 学年度 学期 必修门数 必修总学分 必修平均绩点 信息
             */
            Elements elements1=list.get(0).select("tr");
            /**
             * elements1.size()-2   去掉无关2项
             * i=0时 得到的是标题
             */
            for (int i=0; i<elements1.size()-2; i++) {
                JSONObject temp = new JSONObject(true);
                Element element=elements1.get(i);
                if (i==0) {
                    /**
                     * element.select("th") 标题
                     * 学年度 SchoolYear 学期 Semester 必修门数 RequiredNumber 必修总学分 TotalCredit 必修平均绩点 AverageScorePoint 信息
                     */
                    Elements elements=element.select("th");
                    temp.put("SchoolYear",elements.get(0).ownText());
                    temp.put("Semester",elements.get(1).ownText());
                    temp.put("RequiredNumber",elements.get(2).ownText());
                    temp.put("TotalCredit",elements.get(3).ownText());
                    temp.put("AverageScorePoint",elements.get(4).ownText());
                } else {
                    /**
                     * element.select("td") 数据
                     * 学年度 SchoolYear 学期 Semester 必修门数 RequiredNumber 必修总学分 TotalCredit 必修平均绩点 AverageScorePoint 信息
                     */
                    Elements elements=element.select("td");
                    temp.put("SchoolYear",elements.get(0).ownText());
                    temp.put("Semester",elements.get(1).ownText());
                    temp.put("RequiredNumber",elements.get(2).ownText());
                    temp.put("TotalCredit",elements.get(3).ownText());
                    temp.put("AverageScorePoint",elements.get(4).ownText());
                }
                array1.add(i,temp);
            }
            /**
             * 4.第二个table  list.get(1) 获得 学年学期 课程代码 课程序号 课程名称 课程类别 学分 总评成绩 最终 绩点 的信息
             */
            Elements elements2=list.get(1).select("tr");
            for (int i=0; i<elements2.size(); i++) {
                JSONObject temp = new JSONObject(true);
                Element element = elements2.get(i);
                if (elements2.get(0).select("th").size()==9) {
                    if (i==0) {
                        Elements elements = element.select("th");
                        temp.put("AcademicYear", elements.get(0).ownText());
                        temp.put("CourseCode", elements.get(1).ownText());
                        temp.put("CourseNumber", elements.get(2).ownText());
                        temp.put("CourseTitle", elements.get(3).ownText());
                        temp.put("CourseCategory", elements.get(4).ownText());
                        temp.put("credit", elements.get(5).ownText());
                        temp.put("score", elements.get(6).ownText());
                        temp.put("FinalScore", elements.get(7).ownText());
                        temp.put("AchievementPoint",elements.get(8).ownText());
                    } else {
                        Elements elements = element.select("td");
                        temp.put("AcademicYear", elements.get(0).ownText());
                        temp.put("CourseCode", elements.get(1).ownText());
                        temp.put("CourseNumber", elements.get(2).ownText());
                        temp.put("CourseTitle", elements.get(3).ownText());
                        temp.put("CourseCategory", elements.get(4).ownText());
                        temp.put("credit", elements.get(5).ownText());
                        temp.put("score", elements.get(6).ownText());
                        temp.put("FinalScore", elements.get(7).ownText());
                        temp.put("AchievementPoint",elements.get(8).ownText());
                    }
                } else {
                    if (i==0) {
                        Elements elements = element.select("th");
                        temp.put("AcademicYear", elements.get(0).ownText());
                        temp.put("CourseCode", elements.get(1).ownText());
                        temp.put("CourseNumber", elements.get(2).ownText());
                        temp.put("CourseTitle", elements.get(3).ownText());
                        temp.put("CourseCategory", elements.get(4).ownText());
                        temp.put("credit", elements.get(5).ownText());
                        temp.put("Make-upScore", elements.get(6).ownText());
                        temp.put("score", elements.get(7).ownText());
                        temp.put("FinalScore", elements.get(8).ownText());
                        temp.put("AchievementPoint",elements.get(9).ownText());
                    } else {
                        Elements elements = element.select("td");
                        temp.put("AcademicYear", elements.get(0).ownText());
                        temp.put("CourseCode", elements.get(1).ownText());
                        temp.put("CourseNumber", elements.get(2).ownText());
                        temp.put("CourseTitle", elements.get(3).ownText());
                        temp.put("CourseCategory", elements.get(4).ownText());
                        temp.put("credit", elements.get(5).ownText());
                        temp.put("Make-upScore", elements.get(6).ownText());
                        temp.put("score", elements.get(7).ownText());
                        temp.put("FinalScore", elements.get(8).ownText());
                        temp.put("AchievementPoint",elements.get(9).ownText());
                    }
                }
                array2.add(i,temp);
            }

            JSONObject temp = new JSONObject(true);
            temp.put("SchoolSummary",elements1.get(elements1.size()-2).select("th").get(0).ownText());
            temp.put("RequiredNumber",elements1.get(elements1.size()-2).select("th").get(1).ownText());
            temp.put("TotalCredit",elements1.get(elements1.size()-2).select("th").get(2).ownText());
            temp.put("AverageScorePoint",elements1.get(elements1.size()-2).select("th").get(3).ownText());
            jsonObject2.put("Project",array1);
            jsonObject2.put("ScoreList",array2);
            jsonObject2.put("SchoolCollect",temp);
        }
        jsonObject1.put("data",jsonObject2);
        return jsonObject1;
    }

    public Connection.Response getResponse() {
        return response;
    }

}
