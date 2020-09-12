package com.yjx.changdaplus.pojo.student;

import lombok.Data;

/**
 * @program: changda-plus
 * @description:微信用户
 * @author: YJX
 * @create: 2020-08-30 20:04
 **/
@Data
public class WxUser {
    private Integer id;
    private String openId;  //微信唯一标识符
    private Integer userId; //用户编号
}
