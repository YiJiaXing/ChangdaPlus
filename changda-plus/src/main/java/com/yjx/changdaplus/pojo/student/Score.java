package com.yjx.changdaplus.pojo.student;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: changda-plus
 * @description:成绩信息
 * @author: YJX
 * @create: 2020-09-01 16:35
 **/
@Data
public class Score {
    private Integer id;
    private String stuNo;
    private JSONObject score;
}
