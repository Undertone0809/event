package com.event.mapper;

import com.event.pojo.Chat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.vo.ChatInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-05
 */
@Mapper
@Repository
public interface ChatMapper extends BaseMapper<Chat> {

    List<Chat> selectChat(Chat chat);

    List<ChatInfo> selectAllChat();

    List<Chat> selectChatByTwo(@Param("chatSenderId") Integer chatSenderId,
                               @Param("chatRecipientId") Integer chatRecipientId,
                               @Param("pageSize") Integer pageSize,
                               @Param("pageIndex") Integer pageIndex);
}
