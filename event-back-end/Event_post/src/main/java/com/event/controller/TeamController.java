package com.event.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.form.SearchTeamForm;
import com.event.pojo.Team;
import com.event.pojo.TeamJoin;
import com.event.service.TeamCollectService;
import com.event.service.TeamJoinService;
import com.event.service.TeamLikeService;
import com.event.service.TeamService;
import com.event.utils.SenInfoCheckUtil;
import com.event.vo.TeamInfo;
import com.event.vo.TeamJoinInfo;
import com.event.vo.UserCollectInfo;
import com.event.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 组队表 前端控制器
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    TeamService teamService;
    @Autowired
    TeamJoinService teamJoinService;
    @Autowired
    TeamCollectService teamCollectService;
    @Autowired
    TeamLikeService teamLikeService;

    /**
     *
     * @return 查询组队通用接口
     */
    @GetMapping("/selectTeam")
    public Result<List<TeamInfo>> selectTeam(Team team){
        List<TeamInfo> teamInfos = teamService.selectTeam(team);
        return Result.ok_data(teamInfos,ResultCodeEnum.SUCCESS,teamInfos.size());
    }

    /**
     * 根据关键字分页搜索组队
     */
    @PostMapping("/searchTeam")
    public Result<List<TeamInfo>> searchTeam(@RequestBody SearchTeamForm searchTeamForm){
        List<TeamInfo> teamInfos = teamService.searchTeamByContentAndPage(searchTeamForm);
        return Result.ok_data(teamInfos,ResultCodeEnum.SUCCESS,teamInfos.size());
    }

    /**
     *
     * @param userId 查询我加入的组队
     */
    @GetMapping("/selectMyJoinTeam/{userId}/{pageSize}/{teamIndex}")
    public Result<List<TeamInfo>>  selectMyJoinTeam(@PathVariable("userId") Long userId
            , @PathVariable Integer pageSize,@PathVariable Integer teamIndex){
        List<TeamInfo> teamInfos = teamService.selectMyJoinTeam(userId,pageSize,teamIndex * pageSize);
        return Result.ok_data(teamInfos,ResultCodeEnum.SUCCESS,teamInfos.size());
    }
    @GetMapping("/hasJoinTeam/{userId}/{teamId}")
    public Result<Boolean> hasJoinTeam(@PathVariable Integer userId,@PathVariable Long teamId){
        return Result.ok_data(teamJoinService.hasJoinTeam(userId, teamId) != null,ResultCodeEnum.SUCCESS,1);
    }

    /**
     *
     * @return 查询未结束的所有组队（时间和人数均满足）
     */
    @GetMapping("/selectNotFinishedTeam/{pageSize}/{teamIndex}")
    public Result<List<TeamInfo>> selectNotFinishedTeam(@PathVariable Integer pageSize,@PathVariable Integer teamIndex){
        List<TeamInfo> teamInfos = teamService.selectNotFinishedTeam(pageSize,teamIndex * pageSize);
        return Result.ok_data(teamInfos,ResultCodeEnum.SUCCESS,teamInfos.size());
    }

    /**
     *
     * @return 查询我关注的人发起的组队
     */
    @GetMapping("/selectMyFriendTeam/{collectionUserId}/{pageSize}/{teamIndex}")
    public Result<List<TeamInfo>> selectMyFriendTeam(@PathVariable("collectionUserId") Long collectionUserId
    ,@PathVariable Integer pageSize,@PathVariable Integer teamIndex){
        List<TeamInfo> teamInfos = teamService.selectMyFriendTeam(collectionUserId,pageSize,teamIndex * pageSize);
        return Result.ok_data(teamInfos,ResultCodeEnum.SUCCESS,teamInfos.size());
    }

    /**
     *
     * @return 添加新的队伍不用传发起时间和随机码
     */
    @PostMapping("/insertTeam")
    public Result<Team> insertTeam(@RequestBody Team team) throws Exception {
        // 组队信息初始化
        team.setTeamCode((int) ((Math.random() * 9 + 1) * 100000)).setTeamSendTime(new Date());
        // 脱敏处理
        if (!SenInfoCheckUtil.cotentFilter(team.getTeamTitle()) || !SenInfoCheckUtil.cotentFilter(team.getTeamDetail()))
            return Result.error(ResultCodeEnum.TEXTERROR);
        if (teamService.save(team))
            return Result.ok_data(team,ResultCodeEnum.SUCCESS,1);
        else return Result.error(ResultCodeEnum.ADDERROR);
    }
    /**
     *
     * @return 删除队伍
     */
    @PostMapping("/deleteTeam/{teamId}")
    public Result<Void> deleteTeam(@PathVariable Long teamId){
        if (teamService.deleteTeam(teamId)) return Result.ok(ResultCodeEnum.SUCCESS);
        else return Result.error(ResultCodeEnum.DELETEERROR);
    }
    /**
     *
     * @return 用户加入队伍
     */
    @PostMapping("/insertTeamJoin/{userId}/{teamId}")
    public Result<Void> insertTeamJoin(@PathVariable Integer userId,@PathVariable Long teamId){
        TeamJoin teamJoin = new TeamJoin();
        teamJoin.setUserId(userId).setTeamId(teamId).setJoinTime(new Date());
        Team team = teamService.selectTeamById(teamId);
        team.setTeamGetPnum(team.getTeamGetPnum()+1);
        if (teamJoinService.insertTeamJoin(teamJoin)>0&&teamService.updateTeam(team)>0) return Result.ok(ResultCodeEnum.SUCCESS);
        else return Result.error(ResultCodeEnum.ADDERROR);
    }
    /**
     *
     * @return 用户退出队伍
     */
    @PostMapping("/deleteTeamJoin/{joinId}")
    public Result<Void> deleteTeamJoin(@PathVariable Long joinId){
        TeamJoin teamJoin = teamJoinService.selectTeamJoinById(joinId);
        Team team = teamService.selectTeamById(teamJoin.getTeamId());
        team.setTeamGetPnum(team.getTeamGetPnum()-1);
        if (teamJoinService.deleteTeamJoin(joinId)>0&&teamService.updateTeam(team)>0) return Result.ok(ResultCodeEnum.SUCCESS);
        else return Result.error(ResultCodeEnum.ADDERROR);
    }
    /**
     * @return 组队信息更新接口
     */
    @PostMapping("/updateTeam")
    public Result<Void> updateTeam(@RequestBody Team team) throws Exception {
        if (!SenInfoCheckUtil.cotentFilter(team.getTeamTitle()) || !SenInfoCheckUtil.cotentFilter(team.getTeamDetail()))
            return Result.error(ResultCodeEnum.TEXTERROR);
        if (teamService.updateTeam(team)>0) return Result.ok(ResultCodeEnum.SUCCESS);
        else return Result.error(ResultCodeEnum.MODIFYERROR);
    }
    /**
     *
     * @return 查询是否收藏
     */
    @GetMapping("/hasCollectionTeam/{userId}/{teamId}")
    public Result<Boolean> hasCollectionTeam(@PathVariable Integer userId,@PathVariable Long teamId){
        return Result.ok_data(teamCollectService.hasCollectionTeam(userId, teamId) != null,ResultCodeEnum.SUCCESS,1);
    }
    /**
     *
     * @return s收藏组队接口
     */
    @PostMapping("/collectionTeam/{userId}/{teamId}")
    public Result<Void> collectionTeam(@PathVariable("userId")Integer userId
            ,@PathVariable("teamId")Long teamId){
        if (teamCollectService.collectionTeam(userId,teamId)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.ERROR);
    }
    /**
     *
     * @return 查看用户的收藏接口
     */
    @GetMapping("/selectCollectionTeam/{userId}")
    public Result<List<TeamInfo>> selectCollectionTeam(@PathVariable("userId")Integer userId){
        List<TeamInfo> collectionTeam = teamCollectService.selectCollectionTeam(userId);
        return Result.ok_data(collectionTeam,ResultCodeEnum.SUCCESS,collectionTeam.size());
    }
    /**
     *
     * @return 查看组队收藏数接口
     */
    @GetMapping("/beSelectCollectionTeamNum/{teamId}")
    public Result<Integer> beSelectCollectionTeamNum(@PathVariable("teamId")Long teamId){
        return Result.ok_data(teamCollectService.beSelectCollectionTeamNum(teamId),ResultCodeEnum.SUCCESS
                ,teamCollectService.beSelectCollectionTeamNum(teamId));
    }
    /**
     *
     * @return 取消组队收藏接口
     */
    @PostMapping("/removeCollectionTeam/{userId}/{teamId}")
    public Result<Void> removeCollectionTeam(@PathVariable("userId")Integer userId
            ,@PathVariable("teamId")Long teamId){
        if (teamCollectService.removeCollectionTeam(userId,teamId)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.ERROR);
    }
    /**
     *
     * @return 点赞组队接口
     */
    @PostMapping("/LikeTeam/{userId}/{teamId}")
    public Result<Void> LikeTeam(@PathVariable("userId")Integer userId
            ,@PathVariable("teamId")Long teamId){
        if (teamLikeService.LikeTeam(userId,teamId)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.ERROR);
    }
    /**
     *
     * @return 查看组队点赞数接口
     */
    @GetMapping("/beSelectLikeTeamNum/{teamId}")
    public Result<Integer> beSelectLikeTeamNum(@PathVariable("teamId")Long teamId){
        return Result.ok_data(teamLikeService.beSelectLikeTeamNum(teamId),ResultCodeEnum.SUCCESS
                ,teamLikeService.beSelectLikeTeamNum(teamId));
    }
    /**
     *
     * @return 取消组队点赞接口
     */
    @PostMapping("/removeLikeTeam/{userId}/{teamId}")
    public Result<Void> removeLikeTeam(@PathVariable("userId")Integer userId
            ,@PathVariable("teamId")Long teamId){
        if (teamLikeService.removeLikeTeam(userId,teamId)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.ERROR);
    }
    /**
     *
     * @return 查询是否点赞
     */
    @GetMapping("/hasLikeTeam/{userId}/{teamId}")
    public Result<Boolean> hasLikeTeam(@PathVariable Integer userId,@PathVariable Long teamId){
        return Result.ok_data(teamLikeService.hasLikeTeam(userId, teamId) != null,ResultCodeEnum.SUCCESS,1);
    }
    /**
     *
     * @return 查询加入组队的人
     */
    @GetMapping("/selectHasJoined")
    public Result<List<TeamJoinInfo>> selectHasJoin(TeamJoin tj){
        List<TeamJoinInfo> teamInfos = teamJoinService.selectHasJoin(tj);
        return Result.ok_data(teamInfos,ResultCodeEnum.SUCCESS,teamInfos.size());
    }

}
