package com.yjx.changdaplus.service;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.pojo.book.BookInfo;
import com.yjx.changdaplus.pojo.book.QueryBook;

/**
 * @program: changda-plus
 * @description:书籍
 * @author: YJX
 * @create: 2020-09-01 20:10
 **/
public interface IBookService {
    /**
     * 1.书籍信息
     * @param bookInfo
     * @return
     */
    boolean addBookInfo (BookInfo bookInfo);
    boolean updateBookInfo (BookInfo bookInfo);
    BookInfo findBookInfoByNo (String no);

    /**
     * 2.历史记录   收藏记录
     */
    boolean addQueryBook (QueryBook queryBook);
    boolean updateQueryBook (QueryBook queryBook);
    QueryBook findQueryBook (Integer wxId);

    JSONObject getBook(String bookName,String page);
    JSONObject getCollectionInfo(String no);
    JSONObject getBookInfo(String no, String isbn);

}
