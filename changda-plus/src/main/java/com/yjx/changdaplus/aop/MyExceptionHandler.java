package com.yjx.changdaplus.aop;

import com.yjx.changdaplus.mail.EmailSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-01 20:29
 **/
@RestControllerAdvice
public class MyExceptionHandler {

    @Autowired
    private EmailSend emailSend;

    @ExceptionHandler(value =Exception.class)
    public String exceptionHandler(Exception e){
        emailSend.sendMail("1832277348@qq.com",e.getMessage());
        return e.getMessage();
    }
}
