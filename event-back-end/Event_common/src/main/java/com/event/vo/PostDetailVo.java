package com.event.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class PostDetailVo {

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
    private Date postTime;
    private String postType;
    private Date postUptime;
    private Long postLikeNum;
    private Long postCollectNum;
    private String postImgUrl;

    private String avatar;
    private String openname;

    private String contestId;
    private String contestTitle;

    // 教育经历
    private String userEdu;
}
