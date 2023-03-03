package com.event.service.impl;

import com.event.pojo.Chat;
import com.event.mapper.ChatMapper;
import com.event.service.ChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.vo.ChatInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-05
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {
    @Autowired
    private ChatMapper chatMapper;

    @Override
    @Cacheable("Chat")
    public List<Chat> selectChatByTwo(Integer chatSenderId,Integer chatRecipientId,Integer pageSize,Integer pageIndex) {
        return chatMapper.selectChatByTwo(chatSenderId,chatRecipientId,pageSize,pageIndex);
    }

    @Override
    @Cacheable("Chat")
    public List<Chat> selectChat(Chat chat) {
        return chatMapper.selectChat(chat);
    }

    @Override
    @Cacheable("Chat")
    public List<ChatInfo> selectAllChat() {
        return chatMapper.selectAllChat();
    }

    @Override
    @Transactional
    @CacheEvict(value = "Chat", allEntries=true)
    public int insectChat(Chat chat) {
        return chatMapper.insert(chat);
    }

    @Override
    @Transactional
    @CacheEvict(value = "Chat", allEntries=true)
    public int deleteChat(Long chatId) {
        return chatMapper.deleteById(chatId);
    }
}
