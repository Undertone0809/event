package com.event.service;

import com.event.pojo.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.event.vo.UserCollectInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 用户关注表(一问） 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
public interface UserCollectService extends IService<UserCollect> {
    Integer selectCollectionUserNum(@Param("collectionUserId") Integer collectionUserId);

    Integer beSelectCollectionUserNum(@Param("beCollectionUserId") Integer beCollectionUserId);

    List<UserCollectInfo> selectCollectionUser(@Param("collectionUserId") Integer collectionUserId);

    List<UserCollectInfo> beSelectCollectionUser(@Param("beCollectionUserId") Integer beCollectionUserId);

    int collectionUser(Integer collectionUserId,Integer BeCollectionUserId);

    int removeCollectionUser(Integer collectionUserId,Integer BeCollectionUserId);

    Integer hasCollectionUser(Integer collectionUserId,Integer BeCollectionUserId);
}
