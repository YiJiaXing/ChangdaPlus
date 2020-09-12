package com.yjx.changdaplus.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.enum1.OpenId;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
 * @create: 2020-08-30 07:05
 **/
public class Util {

    public JSONObject getOpenId (String code) {
        JSONObject dataJson = new JSONObject(true);
        /**
         * 验证code值是否有效
         */
        if (code==null||code.equals("")) {
            dataJson.put("code", OpenId.CODEERROR.getCode());
            dataJson.put("msg",OpenId.CODEERROR.getMsg());
        } else {
            JSONObject wxJson = getWeChatId(code);
            String openid = wxJson.getString("openid");
            if (wxJson.isEmpty()||openid==null||openid.equals("")) {
                dataJson.put("code",OpenId.ERROR.getCode());
                dataJson.put("msg",OpenId.ERROR.getMsg());
            } else {
                dataJson.put("code",OpenId.SUCCESS.getCode());
                dataJson.put("msg",OpenId.SUCCESS.getMsg());
                dataJson.put("openId",openid);
            }
        }
        return dataJson;
    }

    // 获得微信openid
    private static JSONObject getWeChatId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx764aa986f9845f7b&secret=bde9c7653c1f71aec35ccf42cbee5ffb&js_code="
                + code + "&grant_type=authorization_code";
        JSONObject json = new JSONObject(true);
        try {
            json = JSONObject.parseObject(Jsoup.connect(url).get().body().ownText());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
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
        JSONObject jsonObject = JSONObject.parseObject(page.getHtml().$("body").xpath("//body/text()").toString());
        return jsonObject.getString("semesterId");
    }

    /**
     * 获取 当学期课程信息
     * @param page
     * @return
     */
    public JSONObject getCourseInfo (Page page) {
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
            temp.put("CourseName", selectable.$("td").nodes().get(0).xpath("//td/text()").toString());
            temp.put("CourseNumber", selectable.$("td").nodes().get(1).xpath("//td/text()").toString());
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
     * @param response
     * @param week
     * @param map
     * @return
     * @throws IOException
     */
    public JSONArray getClassInfo (Connection.Response response,String week,Map<String,String> map) throws IOException {
        JSONArray array = new JSONArray();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("ignoreHead","1");
        datas.put("setting.kind","std");
        datas.put("startWeek",week);
        datas.put("semester.id",map.get("semesterId"));
        datas.put("ids",map.get("ids"));

        Document d1 = Jsoup.connect("http://jwc3.yangtzeu.edu.cn/eams/courseTableForStd!courseTable.action").cookies(response.cookies()).data(datas).get();
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

}
