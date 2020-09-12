package com.yjx.changdaplus.pojo.student;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: changda-plus
 * @description:课表信息
 * @author: YJX
 * @create: 2020-08-30 20:50
 **/
@Data
public class ClassTable {
    private Integer id;
    private String stuNo;
    private Integer week;
    private JSONObject data;
}
