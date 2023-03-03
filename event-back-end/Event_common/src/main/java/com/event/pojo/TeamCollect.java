package com.event.pojo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组队收藏
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@ApiModel(value="TeamCollect对象", description="组队收藏")
public class TeamCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long teamId;

    private Integer userId;


}
