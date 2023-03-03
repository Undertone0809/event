package com.event.mapper;

import com.event.pojo.TeamCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.vo.TeamInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * <p>
 * 组队收藏 Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Mapper
@Repository
public interface TeamCollectMapper extends BaseMapper<TeamCollect> {

    Integer beSelectCollectionTeamNum(@Param("teamId") Long teamId);

    List<TeamInfo> selectCollectionTeam(@Param("userId") Integer userId);

    Integer hasCollectionTeam(@Param("userId") Integer userId,@Param("teamId") Long teamId);

    int removeCollectionTeam(@Param("userId") Integer userId,@Param("teamId") Long teamId);
}
