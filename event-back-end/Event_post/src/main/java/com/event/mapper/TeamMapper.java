package com.event.mapper;

import com.event.pojo.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.vo.TeamInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 组队表 Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Mapper
@Repository
public interface TeamMapper extends BaseMapper<Team> {
    List<TeamInfo> selectMyJoinTeam(@Param("userId") Long userId
            ,@Param("pageSize")Integer pageSize,@Param("teamIndex")Integer teamIndex);

    List<TeamInfo> selectNotFinishedTeam(@Param("pageSize")Integer pageSize,@Param("teamIndex")Integer teamIndex);

    List<TeamInfo> selectMyFriendTeam(@Param("collectionUserId") Long collectionUserId
            ,@Param("pageSize")Integer pageSize,@Param("teamIndex")Integer teamIndex);

    List<TeamInfo> selectTeam(Team team);

    Team selectTeamById(Long teamId);

    int updateTeam(Team team);

    List<TeamInfo> searchTeamByContentAndPage(@Param("keyword") String keyword,
                                              @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

//    void insertTeamReturnId(Team team);
}
