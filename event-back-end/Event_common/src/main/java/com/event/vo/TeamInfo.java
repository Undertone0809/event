package com.event.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TeamInfo implements Comparable<TeamInfo>{
    private static final long serialVersionUID = 1L;
    //组队id
    @TableId(value = "team_id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long teamId;
    //发起人
    private Integer teamLeader;

    //标题
    private String teamTitle;

    //详情
    private String teamDetail;

    //组队的发布时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date teamSendTime;

    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date teamStartTime;

    //结束时间
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

    private String teamContact;

    private String openname;

    private String avatar;

    private String email;

    private String contestId;

    private String contestTitle;

    // 教育经历
    private String userEdu;

    @Override
    public int compareTo(TeamInfo o) {
        return o.teamSendTime.compareTo(this.teamSendTime);
    }
}
