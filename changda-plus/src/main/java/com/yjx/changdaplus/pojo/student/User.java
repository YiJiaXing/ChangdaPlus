package com.yjx.changdaplus.pojo.student;

import lombok.Data;

/**
 * @program: changda-plus
 * @description:用户信息
 * @author: YJX
 * @create: 2020-08-30 20:07
 **/
@Data
public class User {
    private Integer id;
    private String stuNo;   //学号
    private String stuPwd;  //密码
    private String projectId;
    private String name;    //姓名
    private String sex;     //性别
    private String campus;  //所在校区
    private String ids; //ids
    private String semesterId;  //semesterId
    private String examBatchId;
}
