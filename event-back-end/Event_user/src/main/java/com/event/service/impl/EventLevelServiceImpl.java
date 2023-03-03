package com.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.EventLevelMapper;
import com.event.pojo.EventLevel;
import com.event.service.EventLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 一问等级经验 服务实现类
 * </p>
 *
 * @author event
 * @since 2021-10-22
 */
@Service
public class EventLevelServiceImpl extends ServiceImpl<EventLevelMapper, EventLevel> implements EventLevelService {
    @Autowired
    EventLevelMapper mapper;

    @Override
    public List<EventLevel> selectLevel(EventLevel level,Integer row) {
        QueryWrapper<EventLevel> wrapper = new QueryWrapper<>();
        wrapper.eq(level.getUserId()!=null,"userId",level.getUserId());
        wrapper.eq(level.getLevel()!=null,"level",level.getLevel());
        wrapper.eq(level.getExp()!=null,"exp",level.getExp());
        wrapper.orderByDesc("level","exp");
        String limit = row==null ? "limit 10" : "limit "+row.toString();
        wrapper.last(limit);
        List<EventLevel> levels = mapper.selectList(wrapper);
        return levels;
    }

    @Override
    @Transactional
    public int updateLevel(EventLevel level) {
        return mapper.updateById(level);
    }
}
