package com.event.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamComment {

    // 主键自增  雪花算法
    @TableId(type = IdType.AUTO)
    private Long commentId;
    private Integer userId;
    private Long teamId;
    // 利用mybatis-plus自动生成创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private String commentContent;
    private String commentIp;
    private Long fatherId;
    private Long commendLikes;
    private boolean delete;

}
