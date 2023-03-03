package com.event.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.mapper.ContestMapper;
import com.event.pojo.Contest;
import com.event.pojo.ContestPost;
import com.event.pojo.ContestTeam;
import com.event.pojo.Team;
import com.event.service.ContestPostService;
import com.event.service.ContestService;
import com.event.service.ContestTeamService;
import com.event.vo.PostDetailVo;
import com.event.vo.TeamInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private ContestTeamService contestTeamService;

    @Autowired
    private ContestPostService contestPostService;

    // 按照热度分页查询比赛
    @GetMapping("/selectByPage/{pageNum}/{pageSize}")
    public Result<List<Contest>> selectByPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize){
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        // 按照浏览量降序
        queryWrapper.orderByDesc("contest_view");
        // 分页查询
        Page<Contest> page = new Page<>(pageNum,pageSize);
        List<Contest> list = contestService.getBaseMapper().selectPage(page,queryWrapper).getRecords();
        return Result.ok_data(list, ResultCodeEnum.SUCCESS,list.size());
    }


    // 查询所有比赛
    @Deprecated
    @GetMapping("/selectAll")
    public Result<List<Contest>> selectAll(){
        List<Contest> list = contestService.list();
        return Result.ok_data(list, ResultCodeEnum.SUCCESS,list.size());
    }

    // 根据id查询比赛详情
    @GetMapping("/selectById/{teamId}")
    public Result<Contest> selectById(@PathVariable("teamId") Integer teamId){
        Contest contest = contestService.getBaseMapper().selectById(teamId);
        return Result.ok_data(contest, ResultCodeEnum.SUCCESS,1);
    }

    // 添加比赛对应的组队需求
    @GetMapping("/addContestTeam/{teamId}/{contestId}")
    public Result<Contest> addContestTeam(@PathVariable("teamId") Long teamId,@PathVariable("contestId") Long contestId){
        if(teamId == null || contestId == null){
            return Result.ok(ResultCodeEnum.ERROR);
        }else{
            ContestTeam contestTeam = new ContestTeam(null,contestId,teamId);
            contestTeamService.save(contestTeam);
            return Result.ok(ResultCodeEnum.SUCCESS);
        }
    }

    // 分页查询比赛对应的组队需求
    @GetMapping("/selectContestTeam/{contestId}/{pageNum}/{pageSize}")
    public Result<List<TeamInfo>> selectContestTeam(@PathVariable("contestId") Long contestId,@PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize){
        List<TeamInfo> list = contestTeamService.selectContestTeamByPage(contestId,pageNum,pageSize);
        return Result.ok_data(list, ResultCodeEnum.SUCCESS,list.size());
    }

    // 添加比赛对应的帖子
    @PostMapping("/addContestPost/{postId}/{contestId}")
    public Result<Contest> addContestPost(@PathVariable("postId") Long postId,@PathVariable("contestId") Long contestId){
        if(postId == null || contestId == null){
            return Result.ok(ResultCodeEnum.ERROR);
        }else{
            ContestPost contestPost = new ContestPost(null,contestId,postId);
            contestPostService.save(contestPost);
            return Result.ok(ResultCodeEnum.SUCCESS);
        }
    }

    // 分页查询比赛对应的帖子
    @GetMapping("/selectContestPost/{contestId}/{pageNum}/{pageSize}")
    public Result<List<PostDetailVo>> selectContestPost(@PathVariable("contestId") Long contestId, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize){
        List<PostDetailVo> list = contestPostService.selectContestPostByPage(contestId,pageNum,pageSize);
        return Result.ok_data(list, ResultCodeEnum.SUCCESS,list.size());
    }



}
