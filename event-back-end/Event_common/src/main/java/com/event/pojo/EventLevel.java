package com.event.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 一问等级经验
 * </p>
 *
 * @author event
 * @since 2021-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId("userId")
    private Integer userId;
    private Integer level;
    private Integer exp;


}
