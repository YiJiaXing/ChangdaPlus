package com.yjx.changdaplus.pojo.student;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: changda-plus
 * @description:课程信息
 * @author: YJX
 * @create: 2020-08-30 20:55
 **/
@Data
public class CourseInfo {
    private Integer id;
    private String stuNo;
    private JSONObject course;
}
