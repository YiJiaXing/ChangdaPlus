package com.yjx.changdaplus.pojo.student;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: changda-plus
 * @description:考试信息
 * @author: YJX
 * @create: 2020-08-30 20:59
 **/
@Data
public class ExamInfo {
    private Integer id;
    private String stuNo;
    private JSONObject exam;
}
