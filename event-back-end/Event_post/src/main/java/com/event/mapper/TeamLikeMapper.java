package com.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.pojo.TeamLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 组队点赞 Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Mapper
@Repository
public interface TeamLikeMapper extends BaseMapper<TeamLike> {
    Integer beSelectLikeTeamNum(@Param("teamId") Long teamId);

    Integer hasLikeTeam(@Param("userId") Integer userId,@Param("teamId") Long teamId);

    int removeLikeTeam(@Param("userId") Integer userId,@Param("teamId") Long teamId);

}
