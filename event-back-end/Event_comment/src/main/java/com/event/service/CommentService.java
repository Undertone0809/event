package com.event.service;

import com.event.pojo.PostComment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 帖子一级评论 服务类
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-01
 */
public interface CommentService extends IService<PostComment> {
    int postCommentLikeAdd1(Long commentId);

    List<PostComment> selectPostComment(PostComment postComment);

    List<PostComment> selectPostCommentByLike(PostComment postComment);

    boolean deleteComment(Long commentId);

    int insertComment(PostComment postComment);

    int likeComment(Long commentId);

    List<PostComment> selectTopComment(Long postId);

    List<PostComment> selectSubComment(Long faId);

    List<PostComment> selectSubLE2(Long faId);

    /**
     * post表的评论加一
     * @param postId
     * @return
     */
    int addCommentNum(@Param("postId") Long postId);

    PostComment selectCommentById(Long commentId);
}
