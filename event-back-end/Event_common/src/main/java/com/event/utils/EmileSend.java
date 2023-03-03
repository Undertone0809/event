package com.event.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Async
public class EmileSend {

    @Autowired
    JavaMailSender mailSender;

    public  void Send(String email,String random) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        messageHelper.setSubject("亲爱Event用户您正在绑定邮箱或者通过邮箱注册账户");
        messageHelper.setText("<h4 style='color:red'/>您验证码为:"+random+"</br>此验证码在十五分钟内有效</h4>",true);
        messageHelper.setFrom("501664112@qq.com");
        messageHelper.setTo(email);

        mailSender.send(mimeMessage);
    }

}
