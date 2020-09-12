package com.yjx.changdaplus.aop;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.processor.BookProcessor;
import com.yjx.changdaplus.service.IBookService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-05 22:09
 **/
@Aspect
@Component
public class BookRestAop {

    @Autowired
    private BookProcessor bookProcessor;
    @Autowired
    private IBookService bookService;

    private Logger logger = LoggerFactory.getLogger(BookRestAop.class);
    /**
     * 1.定义切入点
     */
    @Pointcut("execution(* com.yjx.changdaplus.rest.BookRest.getBook(..))")
    public void BookAspect() {
        System.out.println("切点");
    }

    @Pointcut("execution(* com.yjx.changdaplus.rest.BookRest.getCollection(..))")
    public void BookCollection() {
        System.out.println("切点");
    }


    /**
     * 2.在执行方法之前 执行此方法
     */
    @Before(value = "BookAspect()")
    public void before(JoinPoint joinPoint) {
        Map<String,String> map = (Map)joinPoint.getArgs()[0];
        JSONObject jsonObject = bookProcessor.getDataJson();
        if (jsonObject!=null) {
            /**
             * 判断数据页数与当前页数是否相同
             */
            if (!jsonObject.getString("danPage").equals(map.get("page"))||!jsonObject.getJSONArray("book").getJSONObject(0).getString("bookName").contains(map.get("bookName"))) {
                bookProcessor.setDataJson(null);
            }
        }
        logger.info("前置");
    }

    @Before(value = "BookCollection()")
    public void beforeInfo() {
        JSONObject jsonObject = bookProcessor.getDataJson1();
        if (jsonObject!=null) {
           bookProcessor.setDataJson1(null);
        }
    }

    /**
     * 3.在执行方法之后 执行此方法
     */
    @After(value = "BookAspect()")
    public void after(JoinPoint joinPoint) {
        Map<String,String> map = (Map)joinPoint.getArgs()[0];
        Integer page=Integer.valueOf(map.get("page"));
        page++;
        bookService.getBook(map.get("bookName"),page.toString());
        logger.info("后置");
    }

    @After(value = "BookCollection()")
    public void after1() {
       bookProcessor.setDataJson1(null);
    }
}
