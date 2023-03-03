package com.event.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName contest_post
 */
@TableName(value ="contest_post")
@Data
@AllArgsConstructor
public class ContestPost implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer contestPostId;

    /**
     * 
     */
    private Long contestId;

    /**
     * 
     */
    private Long postId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}