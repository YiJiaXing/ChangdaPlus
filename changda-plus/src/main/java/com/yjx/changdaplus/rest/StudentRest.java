package com.yjx.changdaplus.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.enum1.OpenId;
import com.yjx.changdaplus.pojo.student.*;
import com.yjx.changdaplus.service.IStudentService;
import com.yjx.changdaplus.util.StudentUtil;
import com.yjx.changdaplus.util.Util;
import org.jsoup.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: changda-plus
 * @description:学生信息数据接口
 * @author: YJX
 * @create: 2020-08-30 21:04
 **/
@CrossOrigin
@RestController
@RequestMapping("/api/student")
public class StudentRest {
    Logger logger = LoggerFactory.getLogger(StudentRest.class);

    @Autowired
    private IStudentService studentService;
    @Autowired
    private RedisTemplate redisTemplate;

    private Util util;

    /**
     * 1.进入微信时 验证是否绑定学号
     */
    @RequestMapping(value = "/boundno/is",method = RequestMethod.GET)
    public JSONObject isBoundStuNo (@RequestParam("code") String code) {
        JSONObject dataJson = new JSONObject(true);
        util = new Util();
        JSONObject wxJson = util.getOpenId(code);
        /**
         * 验证code 是否有效
         */
        if (wxJson.getString("code").equals(OpenId.SUCCESS.getCode())) {
            dataJson.put("lstate",true);
        } else {
            dataJson.put("lstate",false);
        }

        /**
         * 验证是否 绑定学号
         * 判断 openId 是否获得
         */
        if (dataJson.getBoolean("lstate")) {
            String openId = wxJson.getString("openId");
            WxUser wxUser = studentService.findWxUserByOpenId(openId);
            /**
             * wxUser 为 null   说明数据库中无相关信息  是初次用户
             */
            if (wxUser==null) {
                wxUser = new WxUser();
                wxUser.setOpenId(openId);
                /**
                 * 首次微信用户 录入数据库中
                 */
                studentService.addWxUser(wxUser);
            }

            /**
             * 判断是否绑定 学号
             */
            if (wxUser.getUserId() == null||wxUser.getUserId() == 0) {
                dataJson.put("nostate",false);
            } else {
                dataJson.put("nostate",true);
            }

            /**
             * 若绑定学号 则返回 基本信息
             */
            if (dataJson.getBoolean("nostate")) {
                User user = studentService.findUserByUserId(wxUser.getUserId());
                JSONObject temp = new JSONObject(true);
                temp.put("stuNo",user.getStuNo());
                temp.put("stuName", user.getName());
                temp.put("stuSex",user.getSex());
                temp.put("stuCampus", user.getCampus());
                dataJson.put("stuInfo",temp);
            }

        }
        return dataJson;
    }

    /**
     * 2.学号绑定
     */
    @RequestMapping(value = "/boundno/no",method = RequestMethod.POST)
    public JSONObject boundStuNo (@RequestBody Map<String,String> map) {
        String code = map.get("code");
        String userNo = map.get("username");
        String userPwd = map.get("userpassword");
        String projectId = map.get("projectId");
        JSONObject dataJson = new JSONObject(true);
        /**
         * 判断传入的主要参数是否为空
         */
        if (isEmpty(code)||isEmpty(userNo)||isEmpty(userPwd)||isEmpty(projectId)) {
            dataJson.put("state",false);
            return dataJson;
        }
        util = new Util();
        JSONObject wxJson = util.getOpenId(code);
        /**
         * 验证code 是否有效
         */
        if (wxJson.getString("code").equals(OpenId.SUCCESS.getCode())) {
            dataJson.put("lstate",true);
        } else {
            dataJson.put("lstate",false);
        }
        /**
         * 判断oppenId 是否获得
         */
        if (dataJson.getBoolean("lstate")) {
            String openId = wxJson.getString("openId");
            WxUser wxUser = studentService.findWxUserByOpenId(openId);
            User user = studentService.findUserByStuNo(userNo);
            if (user == null) {
                user = new User();
                user.setStuNo(userNo);
                user.setStuPwd(userPwd);
                /**
                 * 验证账号
                 */
                StudentUtil studentUtil = new StudentUtil(userNo,userPwd);
                Connection.Response response =studentUtil.getResponse();
                redisTemplate.boundValueOps(userNo).set(response.cookies(),10, TimeUnit.MINUTES);
                JSONObject temp = studentService.getStudentInfo(userNo, projectId);
                String ids = studentService.getIds(userNo);
                String semesterId = studentService.getSemesterId(userNo, projectId);
                if (temp.isEmpty()) {
                    dataJson.put("state",false);
                } else {
                    temp.put("stuNo",userNo);
                    user.setProjectId(projectId);
                    user.setName(temp.getString("stuName"));
                    user.setSex(temp.getString("stuSex"));
                    user.setCampus(temp.getString("stuCampus"));
                    if (!isEmpty(ids)) {
                        user.setIds(ids);
                    }
                    if (!isEmpty(semesterId)) {
                        user.setSemesterId(semesterId);
                    }
                    /**
                     * 添加用户
                     */
                    studentService.addUser(user);
                    user = studentService.findUserByStuNo(userNo);
                    wxUser.setUserId(user.getId());
                    /**
                     * wxUser绑定 user
                     */
                    studentService.updateWxUser(wxUser);
                    dataJson.put("stuInfo",temp);
                    dataJson.put("state",true);
                }
            } else {
                JSONObject temp = new JSONObject(true);
                wxUser.setUserId(user.getId());
                studentService.updateWxUser(wxUser);
                temp.put("stuName",user.getName());
                temp.put("stuSex", user.getSex());
                temp.put("stuCampus",user.getCampus());
                dataJson.put("stuInfo",temp);
                dataJson.put("state",true);
            }

        } else {
            dataJson.put("state",false);
        }
        return dataJson;
    }

    /**
     * 3.成绩信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/score",method = RequestMethod.GET)
    public JSONObject findStudentScore (@RequestParam Map<String,String> map) {
        Score score = studentService.findScoreByStuNo(map.get("no"));
        JSONObject scoreJson = new JSONObject(true);
        if (score==null) {
            score = new Score();
            score.setStuNo(map.get("no"));
            scoreJson = studentService.getStudentScore(map.get("no"));
            score.setScore(scoreJson);
            studentService.addScore(score);
        } else {
            scoreJson = score.getScore();
        }
        return scoreJson;
    }

    /**
     * 4.课程 考试 信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/courseandexam",method = RequestMethod.GET)
    public JSONObject findStudentCourseAndExam (@RequestParam Map<String,String> map) {
        JSONObject dataJson = new JSONObject(true);
        CourseInfo courseInfo = studentService.findCourseInfoByStuNo(map.get("no"));
        ExamInfo examInfo = studentService.findExamInfoByStuNo(map.get("no"));
        User user = studentService.findUserByStuNo(map.get("no"));
        if (courseInfo==null||examInfo==null) {
            courseInfo = new CourseInfo();
            examInfo = new ExamInfo();
            courseInfo.setStuNo(map.get("no"));
            examInfo.setStuNo(map.get("no"));
            JSONObject jsonObject = studentService.getCourseInfo(map.get("no"),user.getSemesterId(),user.getProjectId());
            if (jsonObject==null) {
                dataJson.put("state",false);
            } else {
                String examBatchId = jsonObject.getString("examBatchId");
                logger.info(examBatchId);
                JSONArray jsonArray = jsonObject.getJSONArray("Course");
                if (examBatchId != null) {
                    user.setExamBatchId(examBatchId);
                    studentService.updateUser(user);
                    JSONObject jsonObject1 = studentService.getExamInfo(map.get("no"),examBatchId);
                    examInfo.setExam(jsonObject1);
                    courseInfo.setCourse(jsonObject);
                    studentService.addExamInfo(examInfo);
                    studentService.addCourseInfo(courseInfo);
                    dataJson.put("course",jsonArray);
                    dataJson.put("exam",jsonObject1.get("Exam"));
                    dataJson.put("state",true);
                } else {
                    dataJson.put("state",false);
                }
            }
        } else {
            dataJson.put("state",true);
            dataJson.put("course",courseInfo.getCourse().getJSONArray("Course"));
            dataJson.put("exam",examInfo.getExam().get("Exam"));
        }
        return dataJson;
    }

    /**
     * 5.课表信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/class",method = RequestMethod.GET)
    public JSONObject findClassInfo (@RequestParam Map<String,String> map) {
        System.out.println(map);
        User user = studentService.findUserByStuNo(map.get("no"));
        String ids = user.getIds();
        ClassTable classTable = studentService.findClassTableByStuNo(map.get("no"), Integer.valueOf(map.get("week")));
        JSONObject jsonObject = new JSONObject(true);
        if (classTable==null) {
            classTable = new ClassTable();
            if (isEmpty(ids)) {
                ids = studentService.getIds(user.getStuNo());
            }
            jsonObject = studentService.getClassInfo(user.getStuNo(),map.get("week"),user.getSemesterId(),ids);
            classTable.setStuNo(map.get("no"));
            classTable.setWeek(Integer.valueOf(map.get("week")));
            classTable.setData(jsonObject);
            studentService.addClassTable(classTable);
        } else {
            if (classTable.getData()==null||classTable.getData().getJSONArray("classInfo").isEmpty()) {
                if (isEmpty(ids)) {
                    ids = studentService.getIds(user.getStuNo());
                }
                jsonObject = studentService.getClassInfo(user.getStuNo(),map.get("week"),user.getSemesterId(),ids);
                classTable.setData(jsonObject);
                studentService.updateClassTable(classTable);
            } else {
                jsonObject = classTable.getData();
            }
        }
        return jsonObject;
    }

    private static boolean isEmpty(String text) {
        if (text==null||text.equals("")) {
            return true;
        } else {
            return false;
        }
    }

}
