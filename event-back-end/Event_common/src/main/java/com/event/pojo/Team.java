package com.event.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Timestamp;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 组队表
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;
//组队id
    @TableId(value = "team_id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long teamId;
//发起人
    private Integer teamLeader;

//标题
    private String teamTitle;

//详情
    private String teamDetail;

//组队的发布时间
    private Date teamSendTime;

//开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date teamStartTime;

//结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date teamEndTime;

//所需人数
    private Integer teamPnum;

//已组人数
    private Integer teamGetPnum;

//地点
    private String teamPlace;

//组队码（短一些随机生成
    private Integer teamCode;
//申请组队的密码（队长设置）
    private String teamPsw;

    private String teamImgUrl;

//联系方式
    private String teamContact;
}
