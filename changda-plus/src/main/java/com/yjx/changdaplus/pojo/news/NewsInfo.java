package com.yjx.changdaplus.pojo.news;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * @program: changda-plus
 * @description:新闻
 * @author: YJX
 * @create: 2020-09-01 20:02
 **/
@Data
public class NewsInfo {
    private Integer id;
    private JSONObject data;
    private Date date;
}
