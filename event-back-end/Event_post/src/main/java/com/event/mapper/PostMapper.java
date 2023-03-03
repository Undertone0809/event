package com.event.mapper;

import com.event.pojo.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.vo.PostDetailVo;
import com.event.vo.UserDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 帖子 Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-01
 */
@Mapper
@Repository
public interface PostMapper extends BaseMapper<Post> {
    Post selectPostById(@Param("postId") Long postId);

    List<Post> selectPost(Post post);

    @Deprecated
    List<Post> selectPostByView(Post post);

    int updatePost(Post post);

    List<Post> selectPostByPostTitle(@Param("postTitle") String postTitle);

    List<Post> selectPostByUptime(Post post);

    int addPostView(@Param("postId") Long postId);

    List<PostDetailVo> searchPostByContentAndPage(@Param("keyword") String keyword,
                                          @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
    List<PostDetailVo> selectMyFriendPost(@Param("userId") String userId,@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<PostDetailVo> selectPostByViews(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<PostDetailVo> selectPostByUserId(@Param("userId") String userId,@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
}
