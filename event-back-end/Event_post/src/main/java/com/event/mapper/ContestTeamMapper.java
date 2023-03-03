package com.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.pojo.ContestTeam;
import com.event.pojo.Team;
import com.event.vo.TeamInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 86132
* @description 针对表【contest_team】的数据库操作Mapper
* @createDate 2022-07-26 18:20:07
* @Entity generator.domain.ContestTeam
*/
@Mapper
@Repository
public interface ContestTeamMapper extends BaseMapper<ContestTeam> {
    List<TeamInfo> selectContestTeamByPage(@Param("contestId") Long contestId, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
}




