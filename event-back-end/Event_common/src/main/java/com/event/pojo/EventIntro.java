package com.event.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 一问用户简介表
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EventIntro implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.ID_WORKER)
    private Integer userId;

    private String userResume;

    private String userSkill;

    private String userEdu;

    private String userPrize;

    private String userExpr;

    private String userBgimg;


}
