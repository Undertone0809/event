package com.event.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组队情况桥表
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@ApiModel(value="TeamJoin对象", description="组队情况桥表")
public class TeamJoin implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "join_id", type = IdType.ID_WORKER)
    private Long joinId;
    //team的id
    private Long teamId;
    //参与人
    private Integer userId;
    private Date joinTime;


}
