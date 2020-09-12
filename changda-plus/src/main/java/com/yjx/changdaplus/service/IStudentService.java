package com.yjx.changdaplus.service;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.pojo.student.*;

/**
 * @program: changda-plus
 * @description:学生信息服务
 * @author: YJX
 * @create: 2020-08-30 21:20
 **/
public interface IStudentService {
    /**
     * 1. WxUser
     */
    boolean addWxUser (WxUser wxUser);
    boolean updateWxUser (WxUser wxUser);
    WxUser findWxUserByOpenId (String openId);

    /**
     * 2.User
     */
    boolean addUser (User user);
    boolean updateUser (User user);
    User findUserByUserId(Integer id);
    User findUserByStuNo (String stuNo);

    /**
     * 3.Score
     */
    boolean addScore (Score score);
    boolean updateScore (Score score);
    Score findScoreByStuNo (String stuNo);

    /**
     * 4.CourseInfo
     */
    boolean addCourseInfo (CourseInfo courseInfo);
    boolean updateCourseInfo (CourseInfo courseInfo);
    CourseInfo findCourseInfoByStuNo (String stuNo);

    /**
     * 5.ExamInfo
     */
    boolean addExamInfo (ExamInfo examInfo);
    boolean updateExamInfo (ExamInfo examInfo);
    ExamInfo findExamInfoByStuNo (String stuNo);

    /**
     * 6.ClassTable
     */
    boolean addClassTable (ClassTable classTable);
    boolean updateClassTable (ClassTable classTable);
    ClassTable findClassTableByStuNo (String stuNo, Integer week);

    JSONObject getStudentInfo (String no, String projectId);
    JSONObject getStudentScore (String no);
    String getSemesterId (String no, String projectId);
    String getIds (String no);
    JSONObject getCourseInfo (String no, String semesterId, String projectId);
    JSONObject getExamInfo (String no, String examBatchId);
    JSONObject getClassInfo (String no, String week, String semesterId, String ids);

}
