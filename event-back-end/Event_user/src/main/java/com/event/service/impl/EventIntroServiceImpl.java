package com.event.service.impl;

import com.event.pojo.EventIntro;
import com.event.mapper.EventIntroMapper;
import com.event.service.EventIntroService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 一问用户简介表 服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-26
 */
@Service
public class EventIntroServiceImpl extends ServiceImpl<EventIntroMapper, EventIntro> implements EventIntroService {

    @Autowired
    private EventIntroMapper eventIntroMapper;
    @Override
    @Cacheable("Intro")
    public EventIntro selectIntroById(Integer userId) {
        return eventIntroMapper.selectIntroById(userId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "Intro", allEntries=true)
    public int updateIntro(EventIntro eventIntro) {
        return eventIntroMapper.updateIntro(eventIntro);
    }
}
