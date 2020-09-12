package com.yjx.changdaplus.pojo.news;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: changda-plus
 * @description:新闻详情
 * @author: YJX
 * @create: 2020-09-01 20:03
 **/
@Data
public class NewsDetail {
    private Integer id;
    private String name;
    private JSONObject data;
}
