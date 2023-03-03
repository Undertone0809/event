package com.event.service.impl;

import com.event.form.SearchTeamForm;
import com.event.pojo.Team;
import com.event.mapper.TeamMapper;
import com.event.service.TeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.vo.TeamInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 组队表 服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {
    @Autowired
    TeamMapper teamMapper;

    @Override
    @Cacheable("Team")
    public List<TeamInfo> selectMyJoinTeam(Long userId,Integer pageSize,Integer teamIndex) {
        return teamMapper.selectMyJoinTeam(userId,pageSize,teamIndex);
    }

    @Override
    @Cacheable("Team")
    public List<TeamInfo> selectNotFinishedTeam(Integer pageSize,Integer teamIndex) {
        return teamMapper.selectNotFinishedTeam(pageSize,teamIndex);
    }

    @Override
    @Cacheable("Team")
    public List<TeamInfo> selectMyFriendTeam(Long collectionUserId,Integer pageSize,Integer teamIndex) {
        return teamMapper.selectMyFriendTeam(collectionUserId,pageSize,teamIndex);
    }

    @Override
    @Cacheable("Team")
    public List<TeamInfo> selectTeam(Team team) {
        return teamMapper.selectTeam(team);
    }

    @Override
    @Cacheable("Team")
    public Team selectTeamById(Long teamId) {
        return teamMapper.selectTeamById(teamId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "Team", allEntries=true)
    public int insertTeam(Team team) {
        return teamMapper.insert(team);
    }

    @Override
    @Transactional
    @CacheEvict(value = "Team", allEntries=true)
    public int updateTeam(Team team) {
        return teamMapper.updateTeam(team);
    }

    @Override
    @Transactional
    public boolean deleteTeam(Long teamId) {
        return removeById(teamId);
    }

    @Override
    @Cacheable("Team")
    public List<TeamInfo> searchTeamByContentAndPage(SearchTeamForm searchTeamForm) {
        String keyword = searchTeamForm.getKeyword();
        Integer pageNum = searchTeamForm.getPageNum();
        Integer pageSize = searchTeamForm.getPageSize();
        return teamMapper.searchTeamByContentAndPage(keyword,(pageNum-1)*pageSize,pageSize);
    }


}
