package com.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.event.pojo.ContestTeam;
import com.event.pojo.Team;
import com.event.vo.TeamInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86132
* @description 针对表【contest_team】的数据库操作Service
* @createDate 2022-07-26 18:20:07
*/
public interface ContestTeamService extends IService<ContestTeam> {

    List<TeamInfo> selectContestTeamByPage(Long contestId,Integer pageNum,Integer pageSize);
}
