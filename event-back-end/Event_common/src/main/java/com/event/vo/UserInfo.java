package com.event.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户信息vo
 * </p>
 *
 * @author 孤雏
 * @since 2021-1-7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "userid", type = IdType.AUTO)
    private Integer userid;
    private String openname;
    private String realname;
    private Long eduAccount;
    private String avatar;
    private String class0;
    private String sex;
    private String birth;
    private String email;
}