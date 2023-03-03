package com.event.controller;

import com.alibaba.fastjson.JSON;
import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.config.WebSocketServer;
import com.event.pojo.Chat;
import com.event.service.ChatService;
import com.event.vo.ChatInfo;
import com.event.vo.MqChat;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-05
 */
@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/selectAllChat")
    public Result<List<ChatInfo>> selectAllChat(){
        List<ChatInfo> chatInfos = chatService.selectAllChat();
        return Result.ok_data(chatInfos,ResultCodeEnum.SUCCESS,chatInfos.size());
    }
    @GetMapping("/selectChat")
    public Result<List<Chat>> selectChat(Chat chat){
        List<Chat> chats = chatService.selectChat(chat);
        return Result.ok_data(chats,ResultCodeEnum.SUCCESS,chats.size());
    }

    /**
     *
     * @return 查询两人的聊天记录!
     */
    @GetMapping("/selectChatByTwo/{chatSenderId}/{chatRecipientId}/{pageSize}/{chatIndex}")
    public Result<List<Chat>> selectChatByTwo(@PathVariable Integer chatSenderId,@PathVariable Integer chatRecipientId
    ,@PathVariable Integer pageSize,@PathVariable Integer chatIndex){
        List<Chat> chats = chatService.selectChatByTwo(chatSenderId, chatRecipientId,pageSize,chatIndex*pageSize);
        return Result.ok_data(chats,ResultCodeEnum.SUCCESS,chats.size());
    }

    @PostMapping("/push/{senderId}/{recipientId}/{message}")
    public Result<Void> pushToWeb(@PathVariable Integer senderId,
                                    @PathVariable Integer recipientId,
                                    @PathVariable String message) {
        Chat chat = new Chat().setChatSenderId(senderId).setCharContext(message).setChatRecipientId(recipientId);
        MqChat mqChat = new MqChat().setChatSenderId(senderId).setCharContext(message).setChatRecipientId(recipientId).setChatTime(new Date());
        if (chatService.insectChat(chat)<=0) return Result.error(ResultCodeEnum.ADDERROR);
        try {
            WebSocketServer.sendInfo(senderId, JSON.toJSONString(chat),recipientId);
            //System.out.println("用户在线");
        } catch (Exception e) {
            //System.out.println("用户不在线走mq了！！");
            rabbitTemplate.convertAndSend(String.valueOf(recipientId),mqChat);
        }
        return Result.ok(ResultCodeEnum.SUCCESS);
    }

}
