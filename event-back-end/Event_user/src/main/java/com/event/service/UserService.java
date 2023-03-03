package com.event.service;

import com.event.form.SearchUserForm;
import com.event.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.event.vo.TeamInfo;
import com.event.vo.UserDetailVo;
import com.event.vo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2021-12-28
 */
public interface UserService extends IService<User> {
    int insertUser(User user);

    UserInfo selectUserInfo(Integer userid);

    List<User> selectUser(User user);

    User selectUserById(Integer userId);

    User selectUserByCode(Long eduAccount);

    User selectUserByOpenId(String openId);

    int updateUser(User user);

    List<TeamInfo> selectTeam(@Param("teamId") Long teamId);

    List<UserDetailVo> searchUserInfoByKeywordAndPage(SearchUserForm searchUserForm);

    List<UserDetailVo> recommendUserByActivity(Integer userId,Integer pageSize,Integer userIndex);

    List<UserDetailVo> selectMyFollower(Integer userId, Integer pageNum, Integer pageSize);

    List<UserDetailVo> selectMyFans(Integer userId, Integer pageNum, Integer pageSize);

    List<UserDetailVo> findSchoolmate(Integer userId, Integer pageNum, Integer pageSize);
}
