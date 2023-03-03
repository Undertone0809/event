package com.event.service;

import com.event.pojo.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.event.vo.ChatInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-05
 */
public interface ChatService extends IService<Chat> {
    List<Chat> selectChatByTwo(Integer chatSenderId,Integer chatRecipientId,Integer pageSize,Integer pageIndex);

    List<Chat> selectChat(Chat chat);

    List<ChatInfo> selectAllChat();

    int insectChat(Chat chat);

    int deleteChat(Long chatId);

}
