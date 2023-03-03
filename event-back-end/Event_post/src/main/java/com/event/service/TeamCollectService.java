package com.event.service;

import com.event.pojo.TeamCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.event.vo.TeamInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组队收藏 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
public interface TeamCollectService extends IService<TeamCollect> {

    Integer beSelectCollectionTeamNum(Long teamId);

    List<TeamInfo> selectCollectionTeam(Integer userId);

    int collectionTeam(Integer userId,Long teamId);

    Integer hasCollectionTeam(Integer userId,Long teamId);

    int removeCollectionTeam(Integer userId,Long teamId);
}
