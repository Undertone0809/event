package com.event.mapper;

import com.event.pojo.Team;
import com.event.pojo.TeamJoin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.vo.TeamInfo;
import com.event.vo.TeamJoinInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 组队情况桥表 Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Mapper
@Repository
public interface TeamJoinMapper extends BaseMapper<TeamJoin> {
    Integer hasJoinTeam(@Param("userId") Integer userId, @Param("teamId") Long teamId);

    List<TeamJoinInfo> selectHasJoin(TeamJoin team);

    TeamJoin selectTeamJoinById(@Param("joinId") Long joinId);

}
