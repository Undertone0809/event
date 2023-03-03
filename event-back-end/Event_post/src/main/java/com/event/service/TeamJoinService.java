package com.event.service;

import com.event.pojo.TeamJoin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.event.vo.TeamInfo;
import com.event.vo.TeamJoinInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组队情况桥表 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
public interface TeamJoinService extends IService<TeamJoin> {

    int insertTeamJoin(TeamJoin teamJoin);

    Integer hasJoinTeam(Integer userId,Long teamId);

    TeamJoin selectTeamJoinById(Long joinId);

    List<TeamJoinInfo> selectHasJoin(TeamJoin teamJoin);

    int deleteTeamJoin(Long joinId);
}
