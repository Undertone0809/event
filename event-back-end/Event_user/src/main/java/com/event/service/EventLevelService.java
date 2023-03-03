package com.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.event.pojo.EventLevel;

import java.util.List;

/**
 * <p>
 * 一问等级经验 服务类
 * </p>
 *
 * @author event
 * @since 2021-10-22
 */
public interface EventLevelService extends IService<EventLevel> {
    List<EventLevel> selectLevel(EventLevel eventLevel,Integer row);
    int updateLevel(EventLevel level);
}
