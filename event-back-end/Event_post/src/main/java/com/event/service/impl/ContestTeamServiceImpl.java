package com.event.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.ContestTeamMapper;
import com.event.pojo.ContestTeam;
import com.event.pojo.Team;
import com.event.service.ContestTeamService;
import com.event.vo.TeamInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86132
* @description 针对表【contest_team】的数据库操作Service实现
* @createDate 2022-07-26 18:20:07
*/
@Service
public class ContestTeamServiceImpl extends ServiceImpl<ContestTeamMapper, ContestTeam>
    implements ContestTeamService {

    @Autowired
    private ContestTeamMapper mapper;

    @Override
    public List<TeamInfo> selectContestTeamByPage(Long contestId, Integer pageNum, Integer pageSize) {
        return mapper.selectContestTeamByPage(contestId,(pageNum-1)*pageSize,pageSize);
    }
}




