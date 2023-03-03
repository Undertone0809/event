package com.event.mapper;

import com.event.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.vo.TeamInfo;
import com.event.vo.UserDetailVo;
import com.event.vo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2021-12-28
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    UserInfo selectUserInfo(@Param("userid")Integer userid);

    List<User> selectUser(User user);

    User selectUserByOpenid(@Param("openid")String openid);

    User selectUserById(@Param("userId")Integer userId);

    User selectUserByCode(@Param("eduAccount")Long eduAccount);

    int updateUser(User user);

    List<TeamInfo> selectTeam(@Param("teamLeader") Long teamLeader);

    List<UserDetailVo> searchUserInfoByKeywordAndPage(@Param("userId")Integer userId,
        @Param("keyword") String keyword, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<UserDetailVo> recommendUserByActivity(@Param("userId")Integer userId,
        @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<UserDetailVo> selectMyFollower(@Param("userId")Integer userId,
        @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<UserDetailVo> selectMyFans(@Param("userId")Integer userId,
        @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<UserDetailVo> findSchoolmate(@Param("userId")Integer userId,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
}
