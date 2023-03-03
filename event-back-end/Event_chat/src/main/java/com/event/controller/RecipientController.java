package com.event.controller;

import com.event.pojo.Chat;
import com.event.vo.MqChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RecipientController {
    /**
     * rabbitmq监听到消息后推送
     */

//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue(value = "queue",durable = "true"),//临时对列
//                    exchange = @Exchange(value = "exchange",type = ExchangeTypes.TOPIC),
//                    key = "a"
//            )
//    })
//    public void mqSend(MqChat chat){
//        System.out.println(chat);
//    }
}
