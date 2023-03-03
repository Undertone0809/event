package com.event.service.impl;

import com.event.pojo.UserCollect;
import com.event.mapper.UserCollectMapper;
import com.event.service.UserCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.vo.UserCollectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户关注表(一问） 服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2022-02-03
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements UserCollectService {
    @Autowired
    UserCollectMapper userCollectMapper;

    @Override
    public Integer selectCollectionUserNum(Integer collectionUserId) {
        return userCollectMapper.selectCollectionUserNum(collectionUserId);
    }

    @Override
    public Integer beSelectCollectionUserNum(Integer beCollectionUserId) {
        return userCollectMapper.beSelectCollectionUserNum(beCollectionUserId);
    }

    @Override
    @Cacheable("UserCollectInfo")
    public List<UserCollectInfo> selectCollectionUser(Integer collectionUserId) {
        return userCollectMapper.selectCollectionUser(collectionUserId);
    }
    @Override
    @Cacheable("UserCollectInfo")
    public List<UserCollectInfo> beSelectCollectionUser(Integer beCollectionUserId) {
        return userCollectMapper.beSelectCollectionUser(beCollectionUserId);
    }
    @Override
    @Transactional
    @CacheEvict(value = "UserCollectInfo", allEntries=true)
    public int collectionUser(Integer collectionUserId, Integer BeCollectionUserId) {
        UserCollect userCollect = new UserCollect();
        userCollect.setCollectUserId(collectionUserId).setBeCollectUserId(BeCollectionUserId);
        return userCollectMapper.insert(userCollect);
    }
    @Override
    @Transactional
    @CacheEvict(value = "UserCollectInfo", allEntries=true)
    public int removeCollectionUser(Integer collectionUserId, Integer BeCollectionUserId) {
        return userCollectMapper.removeCollectionUser(collectionUserId,BeCollectionUserId);
    }

    @Override
    public Integer hasCollectionUser(Integer collectionUserId, Integer BeCollectionUserId) {
        return userCollectMapper.hasCollectionUser(collectionUserId,BeCollectionUserId);
    }
}
