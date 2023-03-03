package com.event.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 帖子
 * </p>
 *
 * @author 孤雏
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@Deprecated
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "post_id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long postId;
    private String postTitle;
    private Integer userId;
    private String postContent;
    private Long postViews;
    private String postIp;
    private Long postCommentNum;
    @TableField(fill = FieldFill.INSERT)
    private Date postTime;
    private String postType;
    private String realname;
    private String avatar;
    private String openname;
    private Integer idx;
    @TableField(fill = FieldFill.INSERT)
    private Date postUptime;
    private Long postLikeNum;
    private Long postCollectNum;
    private String postImgUrl;

}
