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
 * @TableName contest_team
 */
@TableName(value ="contest_team")
@Data
@AllArgsConstructor
public class ContestTeam implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 比赛id
     */
    private Long contestId;

    /**
     * 组队需求id
     */
    private Long teamId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}