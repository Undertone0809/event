package com.event.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "chat_id", type = IdType.ID_WORKER)
    private Long chatId;

    @TableField("chat_senderId")
    private Integer chatSenderId;

    @TableField("chat_recipientId")
    private Integer chatRecipientId;

    private String charContext;
    @TableField(fill = FieldFill.INSERT)
    private Date chatTime;

//    private int index;
}
