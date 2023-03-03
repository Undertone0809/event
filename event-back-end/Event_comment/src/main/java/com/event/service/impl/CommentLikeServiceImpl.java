package com.event.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.CommentLikeMapper;
import com.event.pojo.PostCommentLike;
import com.event.pojo.PostLike;
import com.event.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentLikeServiceImpl extends ServiceImpl<CommentLikeMapper, PostCommentLike> implements CommentLikeService {

    @Autowired
    CommentLikeMapper commentLikeMapper;
    @Override
    public Integer beSelectPostCommentLikeNum(Long commentId) {
        return commentLikeMapper.beSelectPostCommentLikeNum(commentId);
    }

    @Override
    public int LikePostComment(Integer userId, Long commentId) {
        PostCommentLike postCommentLike = new PostCommentLike();
        postCommentLike.setCommentId(commentId).setUserId(userId);
        return commentLikeMapper.insert(postCommentLike);
    }

    @Override
    public Integer hasLikePostCommentLike(Integer userId, Long commentId) {
        return commentLikeMapper.hasLikePostCommentLike(userId, commentId);
    }

    @Override
    public int removeLikePostComment(Integer userId, Long commentId) {
        return commentLikeMapper.removeLikePostComment(userId, commentId);
    }
}
