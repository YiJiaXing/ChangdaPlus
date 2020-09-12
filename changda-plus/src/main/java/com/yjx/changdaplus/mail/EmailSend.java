package com.yjx.changdaplus.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-01 20:14
 **/
@Component
public class EmailSend {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String email, String text) {
        SimpleMailMessage mailMessage =new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setFrom("15387123702@163.com");
        mailMessage.setSubject("长大plus数据错误");
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }
}
