package com.event.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class UserCollectInfo {
    private static final long serialVersionUID = 1L;
    @TableId(value = "userid", type = IdType.AUTO)
    private Integer userid;

    private String openname;

    private String avatar;
}
