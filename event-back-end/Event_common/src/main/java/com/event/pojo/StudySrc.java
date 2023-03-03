package com.event.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 学习资料模块
 * </p>
 *
 * @author 孤雏
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StudySrc implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "src_id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long srcId;
    private String srcName;
    private String srcUrl;
    private String srcPsw;
    private Boolean hasNew;
    private String srcBrief;
    private String srcMenu;
    private String srcType;
}
