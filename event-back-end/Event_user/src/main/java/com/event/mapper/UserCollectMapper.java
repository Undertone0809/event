package com.event.mapper;

import com.event.pojo.UserCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.vo.UserCollectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户关注表(一问） Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Mapper
@Repository
public interface UserCollectMapper extends BaseMapper<UserCollect> {
    Integer selectCollectionUserNum(@Param("collectionUserId") Integer collectionUserId);

    Integer beSelectCollectionUserNum(@Param("beCollectionUserId") Integer beCollectionUserId);

    List<UserCollectInfo> selectCollectionUser(@Param("collectionUserId") Integer collectionUserId);

    List<UserCollectInfo> beSelectCollectionUser(@Param("beCollectionUserId") Integer beCollectionUserId);

    int removeCollectionUser(@Param("collectionUserId") Integer collectionUserId,@Param("beCollectionUserId") Integer beCollectionUserId);

    Integer hasCollectionUser(@Param("collectionUserId") Integer collectionUserId,@Param("beCollectionUserId") Integer beCollectionUserId);
}
