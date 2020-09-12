package com.yjx.changdaplus.pojo.book;

import lombok.Data;

/**
 * @program: changda-plus
 * @description:书籍查询记录
 * @author: YJX
 * @create: 2020-09-01 20:01
 **/
@Data
public class QueryBook {
    private Integer id;     //编号
    private String bookNames;   //搜索书籍的记录
    private String nos;     //收藏书籍的记录
    private Integer wxId;   //微信用户
}
