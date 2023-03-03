package com.event.pojo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户关注表(一问）
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@ApiModel(value="UserCollect对象", description="用户关注表(一问）")
public class UserCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer collectUserId;

    private Integer beCollectUserId;


}
