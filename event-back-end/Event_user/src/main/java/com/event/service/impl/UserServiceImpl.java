package com.event.service.impl;

import com.event.form.SearchUserForm;
import com.event.mapper.UserCollectMapper;
import com.event.pojo.User;
import com.event.mapper.UserMapper;
import com.event.service.UserCollectService;
import com.event.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.vo.TeamInfo;
import com.event.vo.UserDetailVo;
import com.event.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2021-12-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCollectMapper userCollectMapper;

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    @Cacheable("User")
    public UserInfo selectUserInfo(Integer userid) {
        return userMapper.selectUserInfo(userid);
    }

    @Override
    @Cacheable("User")
    public List<User> selectUser(User user) {
        return userMapper.selectUser(user);
    }

    @Override
    @Cacheable("User")
    public User selectUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    @Cacheable("User")
    public User selectUserByCode(Long eduAccount) {
        return userMapper.selectUserByCode(eduAccount);
    }

    @Override
    @Cacheable("User")
    public User selectUserByOpenId(String openId) {
        return userMapper.selectUserByOpenid(openId);
    }

    @Override
    @CacheEvict(value = "User", allEntries=true)
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public List<TeamInfo> selectTeam(Long teamId) {
        return userMapper.selectTeam(teamId);
    }

    @Override
    public List<UserDetailVo> searchUserInfoByKeywordAndPage(SearchUserForm searchUserForm){
        String keyword = searchUserForm.getKeyword();
        Integer pageNum = searchUserForm.getPageNum();
        Integer pageSize = searchUserForm.getPageSize();
        Integer userId = searchUserForm.getUserId();
        List<UserDetailVo> list = userMapper.searchUserInfoByKeywordAndPage(userId, keyword, (pageNum-1)*pageSize, pageSize);
        // 判断是否关注
        for (UserDetailVo userDetailVoItem : list) {
            Boolean hasCollect = userCollectMapper.hasCollectionUser(userId, userDetailVoItem.getUserid())!= null;
            userDetailVoItem.setHasFollow(hasCollect);
            userDetailVoItem.setFollowNum(userCollectMapper.selectCollectionUserNum(userDetailVoItem.getUserid()));
            userDetailVoItem.setFansNum(userCollectMapper.beSelectCollectionUserNum(userDetailVoItem.getUserid()));
        }
        // 粉丝数高的排在前面
        list.sort((o1,o2)->o2.getFansNum()-o1.getFansNum());
        return list;
    }

    @Override
    public List<UserDetailVo> recommendUserByActivity(Integer userId,Integer pageNum, Integer pageSize) {
        List<UserDetailVo> list = userMapper.recommendUserByActivity(userId,(pageNum-1)*pageSize, pageSize);
        // 判断是否关注
        for (UserDetailVo userDetailVoItem : list) {
            Boolean hasCollect = userCollectMapper.hasCollectionUser(userId, userDetailVoItem.getUserid())!= null;
            userDetailVoItem.setHasFollow(hasCollect);
            userDetailVoItem.setFollowNum(userCollectMapper.selectCollectionUserNum(userDetailVoItem.getUserid()));
            userDetailVoItem.setFansNum(userCollectMapper.beSelectCollectionUserNum(userDetailVoItem.getUserid()));
        }
        // 粉丝数高的排在前面
        list.sort((o1,o2)->o2.getFansNum()-o1.getFansNum());
        return list;
    }

    @Override
    public List<UserDetailVo> selectMyFollower(Integer userId, Integer pageNum, Integer pageSize) {
        List<UserDetailVo> list = userMapper.selectMyFollower(userId, (pageNum-1)*pageSize, pageSize);
        // 判断是否关注
        for (UserDetailVo userDetailVoItem : list) {
            Boolean hasCollect = userCollectMapper.hasCollectionUser(userId, userDetailVoItem.getUserid())!= null;
            userDetailVoItem.setHasFollow(hasCollect);
            userDetailVoItem.setFollowNum(userCollectMapper.selectCollectionUserNum(userDetailVoItem.getUserid()));
            userDetailVoItem.setFansNum(userCollectMapper.beSelectCollectionUserNum(userDetailVoItem.getUserid()));
        }
        // 粉丝数高的排在前面
        list.sort((o1,o2)->o2.getFansNum()-o1.getFansNum());
        return list;
    }

    @Override
    public List<UserDetailVo> selectMyFans(Integer userId, Integer pageNum, Integer pageSize) {
        List<UserDetailVo> list = userMapper.selectMyFans(userId, (pageNum-1)*pageSize, pageSize);
        // 判断是否关注
        for (UserDetailVo userDetailVoItem : list) {
            Boolean hasCollect = userCollectMapper.hasCollectionUser(userId, userDetailVoItem.getUserid())!= null;
            userDetailVoItem.setHasFollow(hasCollect);
            userDetailVoItem.setFollowNum(userCollectMapper.selectCollectionUserNum(userDetailVoItem.getUserid()));
            userDetailVoItem.setFansNum(userCollectMapper.beSelectCollectionUserNum(userDetailVoItem.getUserid()));
        }
        // 粉丝数高的排在前面
        list.sort((o1,o2)->o2.getFansNum()-o1.getFansNum());
        return list;
    }

    @Override
    public List<UserDetailVo> findSchoolmate(Integer userId, Integer pageNum, Integer pageSize) {
        List<UserDetailVo> list = userMapper.findSchoolmate(userId, (pageNum-1)*pageSize, pageSize);
        // 判断是否关注
        for (UserDetailVo userDetailVoItem : list) {
            Boolean hasCollect = userCollectMapper.hasCollectionUser(userId, userDetailVoItem.getUserid())!= null;
            userDetailVoItem.setHasFollow(hasCollect);
            userDetailVoItem.setFollowNum(userCollectMapper.selectCollectionUserNum(userDetailVoItem.getUserid()));
            userDetailVoItem.setFansNum(userCollectMapper.beSelectCollectionUserNum(userDetailVoItem.getUserid()));
        }
        // 粉丝数高的排在前面
        list.sort((o1,o2)->o2.getFansNum()-o1.getFansNum());
        return list;
    }
}
