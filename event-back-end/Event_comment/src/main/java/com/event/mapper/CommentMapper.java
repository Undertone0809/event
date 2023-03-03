package com.event.mapper;

import com.event.pojo.PostComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.pojo.PostCommentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 帖子一级评论 Mapper 接口
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-01
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<PostComment> {

    int postCommentLikeAdd1(@Param("commentId") Long commentId);

    List<PostComment> selectPostComment(PostComment postComment);

    List<PostComment> selectPostCommentByLike(PostComment postComment);

    List<PostComment> selectTopComment(PostComment postComment);

    int addCommentNum(@Param("postId") Long postId);

    PostComment selectCommentById(@Param("commentId") Long commentId);
}
