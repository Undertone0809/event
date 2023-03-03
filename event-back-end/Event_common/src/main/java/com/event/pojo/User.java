package com.event.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 孤雏
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "userid", type = IdType.AUTO)
    private Integer userid;
    private String openid;
    private String openname;
    private String realname;
    private Date createtime;
    private Long eduAccount;
    private String eduPassword;
    private String eduEnc;
    private Integer eduStatus;
    private Date updatetime;
    private String avatar;
    private String class0;
    private String sex;
    private String birth;
    private String email;
    private Boolean isNewUser;
}
