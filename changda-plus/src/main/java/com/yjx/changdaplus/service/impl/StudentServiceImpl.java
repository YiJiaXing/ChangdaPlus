package com.yjx.changdaplus.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.enum1.StudentUrl;
import com.yjx.changdaplus.mapper.student.*;
import com.yjx.changdaplus.pojo.student.*;
import com.yjx.changdaplus.processor.StudentProcessor;
import com.yjx.changdaplus.service.IStudentService;
import com.yjx.changdaplus.util.StudentUtil;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: changda-plus
 * @description:学生信息服务
 * @author: YJX
 * @create: 2020-08-30 21:21
 **/
@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private IWxUserMapper wxUserMapper;

    @Autowired
    private IScoreMapper scoreMapper;

    @Autowired
    private IExamInfoMapper examInfoMapper;

    @Autowired
    private IClassTableMapper classTableMapper;

    @Autowired
    private ICourseInfoMapper courseInfoMapper;

    @Autowired
    private StudentProcessor studentProcessor;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public boolean addWxUser(WxUser wxUser) {
        return wxUserMapper.addWxUser(wxUser)==1;
    }

    @Override
    public boolean updateWxUser(WxUser wxUser) {
        return wxUserMapper.updateWxUser(wxUser)==1;
    }

    @Override
    public WxUser findWxUserByOpenId(String openId) {
        return wxUserMapper.findWxUserByOpenId(openId);
    }

    @Transactional
    @Override
    public boolean addUser(User user) {
        return userMapper.addUser(user)==1;
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateUser(user)==1;
    }

    @Override
    public User findUserByUserId(Integer id) {
        return userMapper.findUserById(id);
    }

    @Override
    public User findUserByStuNo(String stuNo) {
        return userMapper.findUserByStuNo(stuNo);
    }

    @Transactional
    @Override
    public boolean addScore(Score score) {
        return scoreMapper.addScore(score)==1;
    }

    @Override
    public boolean updateScore(Score score) {
        return scoreMapper.updateScore(score)==1;
    }

    @Override
    public Score findScoreByStuNo(String stuNo) {
        return scoreMapper.findScoreByStuNo(stuNo);
    }

    @Transactional
    @Override
    public boolean addCourseInfo(CourseInfo courseInfo) {
        return courseInfoMapper.addCourseInfo(courseInfo)==1 ;
    }

    @Override
    public boolean updateCourseInfo(CourseInfo courseInfo) {
        return courseInfoMapper.updateCourseInfo(courseInfo)==1;
    }

    @Override
    public CourseInfo findCourseInfoByStuNo(String stuNo) {
        return courseInfoMapper.findCourseInfoByStuNo(stuNo);
    }

    @Transactional
    @Override
    public boolean addExamInfo(ExamInfo examInfo) {
        return examInfoMapper.addExamInfo(examInfo) == 1;
    }

    @Override
    public boolean updateExamInfo(ExamInfo examInfo) {
        return examInfoMapper.updateExamInfo(examInfo)==1;
    }

    @Override
    public ExamInfo findExamInfoByStuNo(String stuNo) {
        return examInfoMapper.findExamInfoByStuNo(stuNo);
    }

    @Transactional
    @Override
    public boolean addClassTable(ClassTable classTable) {
        return classTableMapper.addClassTable(classTable) == 1;
    }

    @Override
    public boolean updateClassTable(ClassTable classTable) {
        return classTableMapper.updateClassTable(classTable) == 1;
    }

    @Override
    public ClassTable findClassTableByStuNo(String stuNo, Integer week) {
        return classTableMapper.findClassTableByStuNo(stuNo,week);
    }


    /**
     * 基本信息
     * @param no
     * @return
     */
    @Override
    public JSONObject getStudentInfo (String no, String projectId) {
        Map<String,String> one=(Map<String, String>) redisTemplate.boundValueOps(no).get();
        Site site=Site
                    .me()
                    .addCookie("JSESSIONID",one.get("JSESSIONID"))
                    .addCookie("GSESSIONID",one.get("GSESSIONID"))
                    .addCookie("adc-ck-jwxt_pools",one.get("adc-ck-jwxt_pools"))
                    .addHeader("Connection","keep-alive")
                    .setCharset("utf8")
                    .setRetryTimes(3)
                    .setSleepTime(100)
                    .setTimeOut(2000);
        studentProcessor.setSite(site);
        Spider spider = Spider.create(studentProcessor).addUrl(StudentUrl.STUDENTINFO.getUrl()+projectId).thread(1);
        spider.run();
        JSONObject jsonObject = studentProcessor.getDataJson();
        return jsonObject;
    }

    /**
     * 成绩信息
     * @param no
     * @return
     */
    @Override
    public JSONObject getStudentScore (String no) {
        Map<String,String> one=getCookie(no,redisTemplate,userMapper);
        Site site=Site
                .me()
                .addCookie("JSESSIONID",one.get("JSESSIONID"))
                .addCookie("GSESSIONID",one.get("GSESSIONID"))
                .addCookie("adc-ck-jwxt_pools",one.get("adc-ck-jwxt_pools"))
                .addHeader("Connection","keep-alive")
                .setCharset("utf8")
                .setRetryTimes(3)
                .setSleepTime(100)
                .setTimeOut(2000);
        studentProcessor.setSite(site);
        Spider spider = Spider.create(studentProcessor).addUrl(StudentUrl.STUDENTSCORE.getUrl()).thread(1);
        spider.run();
        JSONObject jsonObject = studentProcessor.getDataJson();
        return jsonObject;
    }

    /**
     * 获得SemesterId
     * @param no
     * @return
     */
    @Override
    public String getSemesterId (String no, String projectId) {
        Map<String,String> one=getCookie(no,redisTemplate,userMapper);
        Site site=Site
                .me()
                .addCookie("JSESSIONID",one.get("JSESSIONID"))
                .addCookie("GSESSIONID",one.get("GSESSIONID"))
                .addCookie("adc-ck-jwxt_pools",one.get("adc-ck-jwxt_pools"))
                .addHeader("Connection","keep-alive")
                .setCharset("utf8")
                .setRetryTimes(3)
                .setSleepTime(100)
                .setTimeOut(2000);
        studentProcessor.setSite(site);
        Spider spider = Spider.create(studentProcessor).addUrl(StudentUrl.SEMESTERID.getUrl()+projectId+"&name").thread(1);
        spider.run();
        return studentProcessor.getData();
    }

    /**
     * 获得的ids
     * @param no
     * @return
     */
    @Override
    public String getIds (String no) {
        Map<String,String> one=getCookie(no,redisTemplate,userMapper);
        Site site=Site
                .me()
                .addCookie("JSESSIONID",one.get("JSESSIONID"))
                .addCookie("GSESSIONID",one.get("GSESSIONID"))
                .addCookie("adc-ck-jwxt_pools",one.get("adc-ck-jwxt_pools"))
                .addHeader("Connection","keep-alive")
                .setCharset("utf8")
                .setRetryTimes(3)
                .setSleepTime(100)
                .setTimeOut(2000);
        studentProcessor.setSite(site);
        Spider spider = Spider.create(studentProcessor).addUrl(StudentUrl.STUDENTIDS.getUrl()).thread(1);
        spider.run();
        return studentProcessor.getData();
    }

    /**
     * 获得课程信息
     * @param no
     * @return
     */
    @Override
    public JSONObject getCourseInfo (String no, String semesterId, String projectId) {
        Map<String,String> one=getCookie(no,redisTemplate,userMapper);
        Site site=Site
                .me()
                .addCookie("JSESSIONID",one.get("JSESSIONID"))
                .addCookie("GSESSIONID",one.get("GSESSIONID"))
                .addCookie("adc-ck-jwxt_pools",one.get("adc-ck-jwxt_pools"))
                .addHeader("Connection","keep-alive")
                .setCharset("utf8")
                .setRetryTimes(3)
                .setSleepTime(100)
                .setTimeOut(2000);
        studentProcessor.setSite(site);
        Spider spider = Spider.create(studentProcessor).addUrl(StudentUrl.COURSEINFO.getUrl()+projectId+"&semester.id="+semesterId).thread(1);
        spider.run();
        JSONObject jsonObject = studentProcessor.getDataJson();
        return jsonObject;
    }

    /**
     * 获得考试信息
     * @param no
     * @return
     */
    @Override
    public JSONObject getExamInfo (String no, String examBatchId) {
        Map<String,String> one=getCookie(no,redisTemplate,userMapper);
        Site site=Site
                .me()
                .addCookie("JSESSIONID",one.get("JSESSIONID"))
                .addCookie("GSESSIONID",one.get("GSESSIONID"))
                .addCookie("adc-ck-jwxt_pools",one.get("adc-ck-jwxt_pools"))
                .addHeader("Connection","keep-alive")
                .setCharset("utf8")
                .setRetryTimes(3)
                .setSleepTime(100)
                .setTimeOut(2000);
        studentProcessor.setSite(site);
        Spider spider = Spider.create(studentProcessor).addUrl(StudentUrl.EXAMINFO.getUrl()+examBatchId).thread(1);
        spider.run();
        JSONObject jsonObject = studentProcessor.getDataJson();
        return jsonObject;
    }

    /**
     * 获得课表信息
     * @param no
     * @return
     */
    @Override
    public JSONObject getClassInfo (String no, String week, String semesterId, String ids) {
        JSONObject jsonObject = new JSONObject(true);
        Map<String,String> one=new HashMap<>();
        Map<String,String> two=getCookie(no,redisTemplate,userMapper);
        one.put("semesterId",semesterId);
        one.put("ids",ids);
        StudentUtil studentUtil = new StudentUtil();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = studentUtil.getClassInfo(two,week,one);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put("classInfo",jsonArray);
        return jsonObject;
    }

    private static Map<String, String> getCookie (String no, RedisTemplate redisTemplate,IUserMapper userMapper) {
        User user = userMapper.findUserByStuNo(no);
        Map<String,String> one=(Map<String, String>) redisTemplate.boundValueOps(no).get();
        if (one==null) {
            StudentUtil studentUtil=new StudentUtil(user.getStuNo(),user.getStuPwd());
            Connection.Response response =studentUtil.getResponse();
            redisTemplate.boundValueOps(no).set(response.cookies(),10, TimeUnit.MINUTES);
            one=response.cookies();
        }
        return one;
    }

}
