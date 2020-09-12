package com.yjx.changdaplus.pojo.book;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: changda-plus
 * @description:书籍信息
 * @author: YJX
 * @create: 2020-09-01 19:59
 **/
@Data
public class BookInfo {
    private Integer id;
    private String bookName;
    private String isbn;
    private String no;
    private JSONObject info;
}
