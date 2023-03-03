package com.event.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "chat_id", type = IdType.ID_WORKER)
    private Long chatId;

    @TableField("chat_senderId")
    private Integer chatSenderId;

    @TableField("chat_recipientId")
    private Integer chatRecipientId;

    private String charContext;

    private Date chatTime;

    private String avatar;

    private String openname;

//    private int index;
}