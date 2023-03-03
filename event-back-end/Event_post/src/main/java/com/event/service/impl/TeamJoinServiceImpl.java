package com.event.service.impl;

import com.event.feign.UserFeign;
import com.event.pojo.TeamJoin;
import com.event.mapper.TeamJoinMapper;
import com.event.service.TeamJoinService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.vo.TeamJoinInfo;
import com.event.vo.UserDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 组队情况桥表 服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Service
public class TeamJoinServiceImpl extends ServiceImpl<TeamJoinMapper, TeamJoin> implements TeamJoinService {

    @Autowired
    TeamJoinMapper teamJoinMapper;

    @Autowired
    UserFeign userFeign;

    @Override
    @Transactional
    @CacheEvict(value = "TeamJoin", allEntries=true)
    public int insertTeamJoin(TeamJoin teamJoin) {
        return teamJoinMapper.insert(teamJoin);
    }

    @Override
    public Integer hasJoinTeam(Integer userId, Long teamId) {
        return teamJoinMapper.hasJoinTeam(userId,teamId);
    }

    @Override
    @Cacheable("TeamJoin")
    public TeamJoin selectTeamJoinById(Long joinId) {
        return teamJoinMapper.selectTeamJoinById(joinId);
    }

    @Override
    @Cacheable("TeamJoin")
    public List<TeamJoinInfo> selectHasJoin(TeamJoin teamJoin) {
        List<TeamJoinInfo> list = teamJoinMapper.selectHasJoin(teamJoin);
        // 判断是否关注
        for (TeamJoinInfo TeamJoinInfoItem : list) {
            System.out.println(TeamJoinInfoItem);
            Boolean hasCollect = userFeign.hasCollectionUser(TeamJoinInfoItem.getTeamLeader(), TeamJoinInfoItem.getUserid()).getCode()!= null;
            System.out.println(hasCollect);
            TeamJoinInfoItem.setHasFollow(hasCollect);
            TeamJoinInfoItem.setFollowNum(userFeign.selectCollectionUserNum(TeamJoinInfoItem.getUserid()).getCode());
            TeamJoinInfoItem.setFansNum(userFeign.beSelectCollectionUserNum(TeamJoinInfoItem.getUserid()).getCode());
        }
        // 粉丝数高的排在前面
        // list.sort((o1,o2)->o2.getFollowNum()-o1.getFollowNum());
        return list;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TeamJoin", allEntries=true)
    public int deleteTeamJoin(Long joinId) {
        return teamJoinMapper.deleteById(joinId);
    }

}
