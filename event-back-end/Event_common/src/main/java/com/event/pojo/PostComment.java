package com.event.pojo;

import com.baomidou.mybatisplus.annotation.*;
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
 * 帖子一级评论
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
public class PostComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "comment_id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentId;
    private Integer userId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long postId;
    private Date commentTime;
    @TableField(condition = SqlCondition.LIKE)
    private String commentContent;
    private String commentIp;
    private Long faCommentId;
    private Long commendLikes;
    @TableField(select = false)
    private String replyName;
    private String avatar;
    private String openname;
    @TableField(exist = false)
    private Integer idx;
}
