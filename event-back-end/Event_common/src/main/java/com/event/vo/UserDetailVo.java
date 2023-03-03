package com.event.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDetailVo{
    private static final long serialVersionUID = 1L;
    @TableId(value = "userid", type = IdType.AUTO)
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
