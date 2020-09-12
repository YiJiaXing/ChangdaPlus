package com.yjx.changdaplus.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.pojo.book.BookInfo;
import com.yjx.changdaplus.pojo.book.QueryBook;
import com.yjx.changdaplus.pojo.student.WxUser;
import com.yjx.changdaplus.service.IBookService;
import com.yjx.changdaplus.service.IStudentService;
import com.yjx.changdaplus.util.BookUtil;
import com.yjx.changdaplus.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @program: changda-plus
 * @description:书籍信息数据接口
 * @author: YJX
 * @create: 2020-08-30 21:13
 **/
@CrossOrigin
@RestController
@RequestMapping("/api/book")
public class BookRest {
    private Logger logger = LoggerFactory.getLogger(BookRest.class);

    @Autowired
    private IBookService bookService;

    @Autowired
    private IStudentService studentService;


    /**
     * 1.按照书名查找书籍
     */
    @RequestMapping(value = "/getbook",method = RequestMethod.GET)
    public JSONObject getBook (@RequestParam Map<String, String> map) {
        Util util = new Util();
        JSONObject jsonObject = util.getOpenId(map.get("code"));
        WxUser wxUser = studentService.findWxUserByOpenId(jsonObject.getString("openId"));
        QueryBook queryBook = bookService.findQueryBook(wxUser.getId());
        if (queryBook ==null) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(map.get("bookName"));
            queryBook = new QueryBook();
            queryBook.setWxId(wxUser.getId());
            queryBook.setBookNames(jsonArray.toString());
            bookService.addQueryBook(queryBook);
        } else {
            JSONArray jsonArray = new JSONArray();
            String s=queryBook.getBookNames();
            if (s==null||s.isEmpty()) {
                jsonArray.add(map.get("bookName"));
            } else {
                jsonArray = JSONArray.parseArray(s);
                jsonArray.remove(map.get("bookName"));
                if (jsonArray.size()==10) {
                    jsonArray.remove(jsonArray.size()-1);
                    jsonArray.add(0,map.get("bookName"));
                } else {
                    jsonArray.add(0,map.get("bookName"));
                }
            }
            queryBook.setBookNames(jsonArray.toString());
            bookService.updateQueryBook(queryBook);
        }
        JSONObject dataJson = bookService.getBook(map.get("bookName"), map.get("page"));
        logger.info("获得数据");
        return dataJson;
    }

    /**
     * 2.书籍详细信息
     */
    @RequestMapping(value = "/bookinfo",method = RequestMethod.GET)
    public JSONObject getBookInfo (@RequestParam Map<String, String> map) {
        JSONObject dataJson = new JSONObject(true);
        BookInfo bookInfo = bookService.findBookInfoByNo(map.get("no"));
        if (bookInfo ==null) {
            bookInfo = new BookInfo();
            dataJson = bookService.getBookInfo(map.get("no"), map.get("isbn"));
            bookInfo.setInfo(dataJson);
            bookInfo.setNo(map.get("no"));
            bookInfo.setIsbn(map.get("isbn"));
            bookInfo.setBookName(map.get("bookName"));
            bookService.addBookInfo(bookInfo);
        } else {
            JSONObject jsonObject =bookInfo.getInfo();
            if (jsonObject== null) {
                dataJson = bookService.getBookInfo(map.get("no"), map.get("isbn"));
                bookInfo.setInfo(dataJson);
                bookService.updateBookInfo(bookInfo);
            } else {
                dataJson = bookInfo.getInfo();
            }
        }
        return dataJson;
    }

    /**
     * 3.馆藏信息
     */
    @RequestMapping(value = "/collection",method = RequestMethod.GET)
    public JSONObject getCollection(@RequestParam Map<String, String> map) throws InterruptedException {
        JSONObject jsonObject =bookService.getCollectionInfo(map.get("no"));
        return jsonObject;
    }

    /**
     * 4.获取 历史记录 收藏信息
     */
    @RequestMapping("/histroy")
    public JSONObject getJiLu (@RequestParam Map<String, String> map) {
        JSONObject jsonData =new JSONObject(true);
        Util util = new Util();
        JSONObject jsonObject = util.getOpenId(map.get("code"));
        WxUser wxUser = studentService.findWxUserByOpenId(jsonObject.getString("openId"));
        QueryBook queryBook = bookService.findQueryBook(wxUser.getId());
        if (queryBook==null) {
            jsonData.put("state",false);
        } else {
            JSONArray jsonArray = new JSONArray();
            String s = queryBook.getBookNames();
            if (s==null) {
                jsonData.put("state",false);
            } else {
                jsonArray = JSONArray.parseArray(s);
                jsonData.put("history",jsonArray);
                jsonData.put("state",true);
            }
        }
        return jsonData;
    }

    @RequestMapping("/shoucang")
    public JSONObject getShgoucang (@RequestParam Map<String, String> map) {
        JSONObject jsonData =new JSONObject(true);
        Util util = new Util();
        JSONObject jsonObject = util.getOpenId(map.get("code"));
        WxUser wxUser = studentService.findWxUserByOpenId(jsonObject.getString("openId"));
        QueryBook queryBook = bookService.findQueryBook(wxUser.getId());
        if (queryBook==null) {

        } else {
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArray1 = new JSONArray();
            String s = queryBook.getNos();
            if (s == null) {

            } else {
                jsonArray = JSONArray.parseArray(s);
            }
            if (map.get("n").equals("1")) { //收藏
                if(jsonArray.contains(map.get("no"))) {
                    jsonArray.remove(map.get("no"));
                }
                jsonArray.add(map.get("no"));
                queryBook.setNos(jsonArray.toJSONString());
                bookService.updateQueryBook(queryBook);

            } else if (map.get("n").equals("2")) {  //显示数据
                String nos="";
                for (int i=0; i<jsonArray.size(); i++) {
                    if (i==jsonArray.size()-1) {
                        nos=nos+jsonArray.getString(i);
                    } else {
                        nos=nos+jsonArray.getString(i)+",";
                    }
                }
                JSONObject jsonObject1 = new JSONObject(true);
                try {
                    jsonObject1 = BookUtil.getBookCount(nos);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject finalJsonObject = jsonObject1;
                jsonArray.forEach(s1->{
                    JSONObject temp = new JSONObject(true);
                    BookInfo bookInfo = bookService.findBookInfoByNo(s1.toString());
                    temp.put("no", s1.toString());
                    temp.put("isbn",bookInfo.getIsbn());
                    temp.put("bookName", bookInfo.getBookName());
                    temp.put("author", "");
                    temp.put("Count", finalJsonObject.get(s1.toString()));
                    jsonArray1.add(temp);
                });
                jsonData.put("book",jsonArray1);

            } else if (map.get("n").equals("3")) {  //删除数据
                jsonArray.remove(map.get("no"));
                queryBook.setNos(jsonArray.toJSONString());
                bookService.updateQueryBook(queryBook);
            }
        }
        return jsonData;
    }
}
