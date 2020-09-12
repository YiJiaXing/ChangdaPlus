package com.yjx.changdaplus;

import com.yjx.changdaplus.mail.EmailSend;
import com.yjx.changdaplus.processor.BookProcessor;
import com.yjx.changdaplus.processor.NewsProcessor;
import com.yjx.changdaplus.processor.StudentProcessor;
import com.yjx.changdaplus.service.IBookService;
import com.yjx.changdaplus.service.INewsService;
import com.yjx.changdaplus.service.IStudentService;
import com.yjx.changdaplus.util.StudentUtil;
import org.jsoup.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ChangdaPlusApplicationTests {

    @Autowired
    StudentProcessor studentProcessor;

    @Autowired
    NewsProcessor newsProcessor;

    @Autowired
    BookProcessor bookProcessor;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    EmailSend emailSend;
    @Autowired
    private IStudentService studentService;

    @Autowired
    private IBookService bookService;

    @Autowired
    private INewsService newsService;

    @Test
    void contextLoads() throws Exception {
        /**
         * 1.基本信息  http://jwc3.yangtzeu.edu.cn/eams/stdDetail!innerIndex.action?projectId=1
         * 2.成绩信息  http://221.233.24.23/eams/teach/grade/course/person!historyCourseGrade.action?projectType=MAJOR
         * 3.http://jwc3.yangtzeu.edu.cn/eams/dataQuery.action?tagId=semesterBar21150715451Semester&dataType=semesterCalendar&value=109&empty=false
         * 4.http://jwc3.yangtzeu.edu.cn/eams/stdExamTable!innerIndex.action?project.id=3&semester.id=109
         * 5.http://jwc3.yangtzeu.edu.cn/eams/stdExamTable!examTable.action?examBatch.id=523
         * 6.http://jwc3.yangtzeu.edu.cn/eams/courseTableForStd.action
         */
        Map<String,String> one=(Map<String, String>) redisTemplate.boundValueOps("hello").get();
        if (one==null) {
            StudentUtil studentUtil=new StudentUtil("201600452","170318");
            Connection.Response response =studentUtil.getResponse();
            redisTemplate.boundValueOps("hello").set(response.cookies(),10, TimeUnit.MINUTES);
            Site site=Site
                    .me()
                    .addCookie("JSESSIONID",response.cookies().get("JSESSIONID"))
                    .addCookie("GSESSIONID",response.cookies().get("GSESSIONID"))
                    .addCookie("adc-ck-jwxt_pools",response.cookies().get("adc-ck-jwxt_pools"))
                    .addHeader("Connection","keep-alive")
                    .setCharset("utf8")
                    .setRetryTimes(3)
                    .setSleepTime(1000)
                    .setTimeOut(5000);

            studentProcessor.setSite(site);
            Spider spider=Spider.create(studentProcessor).addUrl("http://jwc3.yangtzeu.edu.cn/eams/courseTableForStd.action").thread(1);
            spider.run();
            System.out.println(studentProcessor.getData());
            System.out.println(studentProcessor.getDataJson());
        } else {
            Site site=Site
                    .me()
                    .addCookie("JSESSIONID",one.get("JSESSIONID"))
                    .addCookie("GSESSIONID",one.get("GSESSIONID"))
                    .addCookie("adc-ck-jwxt_pools",one.get("adc-ck-jwxt_pools"))
                    .addHeader("Connection","keep-alive")
                    .setCharset("utf8")
                    .setRetryTimes(3)
                    .setSleepTime(1000)
                    .setTimeOut(5000);

            studentProcessor.setSite(site);

            Spider spider=Spider.create(studentProcessor).addUrl("http://jwc3.yangtzeu.edu.cn/eams/courseTableForStd!index.action?projectId=3").thread(1);
            spider.run();
            System.out.println(studentProcessor.getData());
            System.out.println(studentProcessor.getDataJson());
        }


        /**
         * http://calis.yangtzeu.edu.cn:8000/opac/api/holding/
         * http://calis.yangtzeu.edu.cn:8000/opac/search?&q=Java&searchType=standard&isFacet=true&view=standard&searchWay=title&rows=10&sortWay=score&sortOrder=desc&searchWay0=marc&logical0=AND&page=1
         */

//        Spider spider=Spider.create(bookProcessor).addUrl("http://calis.yangtzeu.edu.cn:8000/opac/search?&q=Java&searchType=standard&isFacet=true&view=standard&searchWay=title&rows=10&sortWay=score&sortOrder=desc&searchWay0=marc&logical0=AND&page=1").thread(2);
//        spider.run();

//        BookUtil bookUtil = new BookUtil();
//        bookUtil.getBookCount("986745,751136,703708,716228,708721,370563,359854,748860,212761,212762");

//        System.out.println(bookProcessor.getDataJson());

//        DouBanUtil douBanUtil = new DouBanUtil();
//        System.out.println(douBanUtil.getBouBanBookInfo("978-7-5641-4596-5"));
//        emailSend.sendMail("1832277348@qq.com","数据错误");
//        WxUser wxUser =new WxUser();
//        wxUser.setOpenId("123456789");
//        System.out.println(studentService.addWxUser(wxUser));
//        WxUser wxUser =studentService.findWxUserByOpenId("123456789");
//        wxUser.setUserId(1);
//        System.out.println(studentService.updateWxUser(wxUser));
//        System.out.println(wxUser);

//        User user = studentService.findUserByStuNo("201661627");
//        user.setExamBatchId("111");
//        System.out.println(studentService.updateUser(user));
//        user.setStuNo("201661627");
//        user.setStuPwd("201661627");
//        user.setIds("45455");
//        user.setSemesterId("2222");
//        user.setName("呼哈");
//        user.setSex("男");
//        user.setCampus("武汉");
//        System.out.println(studentService.addUser(user));
//        JSONObject jsonObject =new JSONObject(true);
//        jsonObject.put("name","yijiaxing");
//        jsonObject.put("sex","男");
//        jsonObject.put("age",12);
//        Score score = studentService.findScoreByStuNo("201661627");
//        score.setScore(jsonObject);
//        System.out.println(studentService.updateScore(score));
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add(1);
//        jsonArray.add(22);
//        JSONObject jsonObject =new JSONObject(true);
//        jsonObject.put("exam",jsonArray);
//        ExamInfo examInfo = studentService.findExamInfoByStuNo("201661627");
////        examInfo.setStuNo("201661627");
//        examInfo.setExam(jsonObject);
//        System.out.println(studentService.updateExamInfo(examInfo));
//        CourseInfo courseInfo = studentService.findCourseInfoByStuNo("201661627");
////        courseInfo.setStuNo("201661627");
//        courseInfo.setCourse(jsonObject);
//        System.out.println(studentService.updateCourseInfo(courseInfo));
//        ClassTable classTable = studentService.findClassTableByStuNo("201661627",1);
////        classTable.setWeek(1);
////        classTable.setStuNo("201661627");
//        classTable.setData(jsonObject);
//        System.out.println(studentService.updateClassTable(classTable));

//        JSONObject jsonObject = new JSONObject(true);
//        jsonObject.put("data","书籍介绍");
//        BookInfo bookInfo =new BookInfo();
//        bookInfo.setBookName("java开发");
//        bookInfo.setIsbn("1111111");
//        bookInfo.setNo("25252");
//        bookInfo.setInfo(jsonObject);
//        System.out.println(bookService.addBookInfo(bookInfo));
//        BookInfo bookInfo1 = bookService.findBookInfoByNo("25252");
//        jsonObject.put("ssss","sss21");
//        bookInfo1.setInfo(jsonObject);
//        System.out.println(bookService.updateBookInfo(bookInfo1));

//        QueryBook queryBook = new QueryBook();
//        queryBook.setBookNames("物理,化学");
//        queryBook.setNos("12345,14725");
//        queryBook.setWxId(1);
//        System.out.println(bookService.addQueryBook(queryBook));
//        QueryBook queryBook1 = bookService.findQueryBook(1);
//        System.out.println(bookService.updateQueryBook(queryBook));

//        JSONObject jsonObject = new JSONObject(true);
////        NewsInfo newsInfo = new NewsInfo();
//        jsonObject.put("hh","12321");
//        newsInfo.setData(jsonObject);
//        newsInfo.setDate(new Date());
//        System.out.println(newsService.addNewsInfo(newsInfo));
//        NewsInfo newsInfo1 = newsService.findNewsInfo();
//        System.out.println(newsInfo1.getData());
//        NewsDetail newsDetail =new NewsDetail();
//        newsDetail.setName("zheshi");
//        newsDetail.setData(jsonObject);
////        System.out.println(newsService.addNewsDetail(newsDetail));
//        NewsDetail newsDetail1 = newsService.findNewsDetail("zheshi");
//        newsDetail1.setData(jsonObject);
//        System.out.println(newsService.updateNewsDetail(newsDetail1));





    }

}
