package com.event.service;

import com.event.pojo.EventIntro;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 一问用户简介表 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-26
 */
public interface EventIntroService extends IService<EventIntro> {

    EventIntro selectIntroById(@Param("userId")Integer userId);

    int updateIntro(EventIntro eventIntro);
}
