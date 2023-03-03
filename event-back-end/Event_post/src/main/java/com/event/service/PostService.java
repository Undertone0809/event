package com.event.service;

import com.event.form.SearchPostForm;
import com.event.pojo.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.event.vo.PostDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 帖子 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-01
 */

public interface PostService extends IService<Post> {
    Post selectPostById(Long postId);

    List<Post> selectPost(Post post);

    @Deprecated
    List<Post> selectPostByView(Post post,String flag);

    boolean deletePost(Long postId);

    int insertPost(Post post);

    int updatePost(Post post);

    int addPostView(Long postId);

    List<Post> selectPostByPostTitle(String postTitle,String flag);

    List<Post> selectPostByUptime(Post post,String flag);

    List<PostDetailVo> searchPostByContentAndPage(SearchPostForm searchPostForm);

    List<PostDetailVo> selectMyFriendPost(String userId,Integer pageNum,Integer pageSize);

    List<PostDetailVo> selectPostByViews(Integer pageNum,Integer pageSize);

    List<PostDetailVo> selectPostByUserId(String userId,Integer pageNum, Integer pageSize);
}
