package com.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.event.pojo.PostCommentLike;

public interface CommentLikeService extends IService<PostCommentLike> {

    Integer beSelectPostCommentLikeNum(Long commentId);

    int LikePostComment(Integer userId,Long commentId);

    Integer hasLikePostCommentLike(Integer userId,Long commentId);

    int removeLikePostComment(Integer userId,Long commentId);
}
