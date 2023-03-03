package com.event.service.impl;

import com.event.pojo.TeamCollect;
import com.event.mapper.TeamCollectMapper;
import com.event.service.TeamCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.vo.TeamInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 组队收藏 服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Service
public class TeamCollectServiceImpl extends ServiceImpl<TeamCollectMapper, TeamCollect> implements TeamCollectService {

    @Autowired
    private TeamCollectMapper teamCollectMapper;
    @Override
    public Integer beSelectCollectionTeamNum(Long teamId) {
        return teamCollectMapper.beSelectCollectionTeamNum(teamId);
    }

    @Override
    public List<TeamInfo> selectCollectionTeam(Integer userId) {
        return teamCollectMapper.selectCollectionTeam(userId);
    }

    @Override
    @Transactional
    public int collectionTeam(Integer userId, Long teamId) {
        TeamCollect teamCollect = new TeamCollect();
        teamCollect.setUserId(userId).setTeamId(teamId);
        return teamCollectMapper.insert(teamCollect);
    }

    @Override
    public Integer hasCollectionTeam(Integer userId, Long teamId) {
        return teamCollectMapper.hasCollectionTeam(userId,teamId);
    }

    @Override
    @Transactional
    public int removeCollectionTeam(Integer userId, Long teamId) {
        return teamCollectMapper.removeCollectionTeam(userId,teamId);
    }
}
