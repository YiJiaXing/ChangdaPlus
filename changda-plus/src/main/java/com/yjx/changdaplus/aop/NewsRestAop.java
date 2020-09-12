package com.yjx.changdaplus.aop;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.processor.NewsProcessor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-07 12:29
 **/
@Aspect
@Component
public class NewsRestAop {

    @Autowired
    private NewsProcessor newsProcessor;

    @Pointcut("execution(* com.yjx.changdaplus.rest.NewsRest.getNewsInfo(..))")
    public void NewsAspect() {

    }

    @Pointcut("execution(* com.yjx.changdaplus.rest.NewsRest.getNewsInfo(..))")
    public void NewsInfoAspect() {

    }

    @Before("NewsAspect()")
    public void before() {
        JSONObject jsonObject = newsProcessor.getDataJson();
        if (jsonObject!=null) {
            newsProcessor.setDataJson(null);
        }
    }

    @Before("NewsInfoAspect()")
    public void before1() {
        JSONObject jsonObject = newsProcessor.getDataJson();
        if (jsonObject!=null) {
            newsProcessor.setDataJson(null);
        }
    }

    @After("NewsAspect()")
    public void after() {
        newsProcessor.setDataJson(null);
    }

    @After("NewsInfoAspect()")
    public void after1() {
        newsProcessor.setDataJson(null);
    }

}
