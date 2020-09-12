package com.yjx.changdaplus.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.mapper.book.IBookMapper;
import com.yjx.changdaplus.mapper.book.IQueryBookMapper;
import com.yjx.changdaplus.pojo.book.BookInfo;
import com.yjx.changdaplus.pojo.book.QueryBook;
import com.yjx.changdaplus.processor.BookProcessor;
import com.yjx.changdaplus.service.IBookService;
import com.yjx.changdaplus.util.BookUtil;
import com.yjx.changdaplus.util.DouBanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

import java.io.IOException;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-01 20:11
 **/
@Transactional
@Service
public class BookServiceImpl implements IBookService {

    private Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);


    @Autowired
    private IBookMapper bookMapper;

    @Autowired
    private IQueryBookMapper iqueryBookMapper;

    @Autowired
    private BookProcessor bookProcessor;

    @Override
    public boolean addBookInfo(BookInfo bookInfo) {
        return bookMapper.addBookInfo(bookInfo) == 1;
    }

    @Override
    public boolean updateBookInfo(BookInfo bookInfo) {
        return bookMapper.updateBookInfo(bookInfo) ==1;
    }

    @Override
    public BookInfo findBookInfoByNo(String no) {
        return bookMapper.findBookInfo(no);
    }

    @Override
    public boolean addQueryBook(QueryBook queryBook) {
        return iqueryBookMapper.addQueryBook(queryBook) ==1;
    }

    @Override
    public boolean updateQueryBook(QueryBook queryBook) {
        return iqueryBookMapper.updateQueryBook(queryBook) ==1;
    }

    @Override
    public QueryBook findQueryBook(Integer wxId) {
        return iqueryBookMapper.findQueryBook(wxId);
    }


    /**
     * 获取数据的平均时间是 330ms
     * @param bookName
     * @param page
     * @return
     */
    @Override
    public JSONObject getBook(String bookName, String page) {
        String url="http://calis.yangtzeu.edu.cn:8000/opac/search?&q="+bookName+"&searchType=standard&isFacet=true&view=standard&searchWay=title&rows=10&sortWay=score&sortOrder=desc&searchWay0=marc&logical0=AND&page="+page;
        Spider  spider = Spider.create(bookProcessor).addUrl(url).thread(15);
        spider.start();
        logger.info("开始查询书籍");
        JSONObject jsonObject = bookProcessor.getDataJson();
        while (jsonObject==null||jsonObject.isEmpty()) {
            jsonObject = bookProcessor.getDataJson();
           if (jsonObject==null) {
               try {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           } else {
               logger.info("找到");
           }
        }
//        bookProcessor.setDataJson(null);
        return jsonObject;
    }

    @Override
    public JSONObject getCollectionInfo(String no) {
        String url="http://calis.yangtzeu.edu.cn:8000/opac/api/holding/"+no;
        Spider  spider = Spider.create(bookProcessor).addUrl(url).thread(15);
        spider.start();
        JSONObject jsonObject = bookProcessor.getDataJson1();
        while (jsonObject==null||jsonObject.isEmpty()) {
            jsonObject = bookProcessor.getDataJson1();
            if (jsonObject==null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("找到");
            }
        }
//        bookProcessor.setDataJson(null);
        return jsonObject;
    }


    /**
     * 时间比较长  需要进一步优化
     * @param no
     * @param isbn
     * @return
     */
    @Override
    public JSONObject getBookInfo(String no,String isbn) {
        JSONObject jsonObject = new JSONObject(true);
        DouBanUtil douBanUtil =new DouBanUtil();
        BookUtil bookUtil = new BookUtil();
        if (isbn==null||isbn.equals("")||isbn.length()<2) {
            try {
                jsonObject = bookUtil.getBookInfo(no);
                jsonObject.put("image","https://img3.doubanio.com/f/shire/5522dd1f5b742d1e1394a17f44d590646b63871d/pics/book-default-lpic.gif");
                jsonObject.put("kind","info");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                jsonObject = douBanUtil.getBouBanBookInfo(isbn);
                jsonObject.put("kind","douban");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    jsonObject = bookUtil.getBookInfo(no);
                    jsonObject.put("image","https://img3.doubanio.com/f/shire/5522dd1f5b742d1e1394a17f44d590646b63871d/pics/book-default-lpic.gif");
                    jsonObject.put("kind","info");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
        return jsonObject;
    }
}
