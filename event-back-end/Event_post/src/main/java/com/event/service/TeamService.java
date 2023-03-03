package com.event.service;

import com.event.form.SearchTeamForm;
import com.event.pojo.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.event.vo.TeamInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组队表 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
public interface TeamService extends IService<Team> {
    List<TeamInfo> selectMyJoinTeam(Long userId,Integer pageSize,Integer teamIndex);

    List<TeamInfo> selectNotFinishedTeam(Integer pageSize,Integer teamIndex);

    List<TeamInfo> selectMyFriendTeam(Long collectionUserId,Integer pageSize,Integer teamIndex);

    List<TeamInfo> selectTeam(Team team);

    Team selectTeamById(Long teamId);

    int insertTeam(Team team);

    int updateTeam(Team team);

    boolean deleteTeam(Long teamId);

    List<TeamInfo> searchTeamByContentAndPage(SearchTeamForm searchTeamForm);
}
