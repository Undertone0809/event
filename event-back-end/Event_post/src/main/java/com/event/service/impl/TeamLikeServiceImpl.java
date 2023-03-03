package com.event.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.TeamLikeMapper;
import com.event.pojo.TeamLike;
import com.event.service.TeamLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamLikeServiceImpl  extends ServiceImpl<TeamLikeMapper, TeamLike> implements TeamLikeService {

    @Autowired
    TeamLikeMapper teamLikeMapper;
    @Override
    public Integer beSelectLikeTeamNum(Long teamId) {
        return teamLikeMapper.beSelectLikeTeamNum(teamId);
    }

    @Override
    @Transactional
    public int LikeTeam(Integer userId, Long teamId) {
        TeamLike teamLike = new TeamLike();
        teamLike.setTeamId(teamId).setUserId(userId);
        return teamLikeMapper.insert(teamLike);
    }

    @Override
    public Integer hasLikeTeam(Integer userId, Long teamId) {
        return teamLikeMapper.hasLikeTeam(userId, teamId);
    }

    @Override
    @Transactional
    public int removeLikeTeam(Integer userId, Long teamId) {
        return teamLikeMapper.removeLikeTeam(userId, teamId);
    }
}
