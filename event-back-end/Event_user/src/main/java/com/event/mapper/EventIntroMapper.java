package com.event.mapper;

import com.event.pojo.EventIntro;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 一问用户简介表 Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-26
 */
@Mapper
@Repository
public interface EventIntroMapper extends BaseMapper<EventIntro> {

    EventIntro selectIntroById(@Param("userId")Integer userId);

    int updateIntro(EventIntro eventIntro);
}
