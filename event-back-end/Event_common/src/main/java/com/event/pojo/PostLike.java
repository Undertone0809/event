package com.event.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 帖子点赞
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PostLike implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long postId;

    private Integer userId;

}
