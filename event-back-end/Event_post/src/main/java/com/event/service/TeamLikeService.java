package com.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.event.pojo.TeamLike;

/**
 * <p>
 * 组队点赞 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
public interface TeamLikeService  extends IService<TeamLike> {

    Integer beSelectLikeTeamNum(Long teamId);

    int LikeTeam(Integer userId,Long teamId);

    Integer hasLikeTeam(Integer userId,Long teamId);

    int removeLikeTeam(Integer userId,Long teamId);
}
