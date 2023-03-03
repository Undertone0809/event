package com.event.vo;

import java.util.Date;
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
public class MqChat implements Serializable {

    private Integer chatSenderId;

    private Integer chatRecipientId;

    private String charContext;

    private Date chatTime;
}