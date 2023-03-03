package com.event.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @TableName contest
 */
@TableName(value ="contest")
@Data
@AllArgsConstructor
public class Contest implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long contestId;

    /**
     * 比赛标题
     */
    private String contestTitle;

    /**
     * 比赛描述
     */
    private String contestDescription;

    /**
     * 创建时间
     */
    private Date contestCreateTime;

    /**
     * 更新时间
     */
    private Date contestBeginTime;

    /**
     * 结束时间
     */
    private Date contestEndTime;

    /**
     * 赛事地区
     */
    private String contestArea;

    /**
     * 比赛相关url
     */
    private String contestUrl;

    /**
     * 浏览量
     */
    private Long contestView;

    /**
     * 图片url
     */
    private String contestImageUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}