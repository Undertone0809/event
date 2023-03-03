package com.event.vo;

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
public class TeamJoinInfo {
    private static final long serialVersionUID = 1L;
    //组队id
    @TableId(value = "team_id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long teamId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long joinId;
    private Date joinTime;

    private Integer teamLeader;

    private Integer userid;
    private String openname;
    private String avatar;
    private String email;

    private String userResume;
    private String userSkill;
    private String userEdu;
    private String userPrize;
    private String userExpr;
    private String userBgimg;

    private Boolean hasFollow;
    private Integer fansNum;
    private Integer followNum;

}
