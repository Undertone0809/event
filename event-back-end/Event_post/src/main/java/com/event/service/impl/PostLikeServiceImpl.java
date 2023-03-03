package com.event.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.PostLikeMapper;
import com.event.pojo.PostLike;
import com.event.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike> implements PostLikeService {

    @Autowired
    PostLikeMapper postLikeMapper;

    @Override
    public Integer beSelectLikePostNum(Long postId) {
        return postLikeMapper.beSelectLikePostNum(postId);
    }

    @Override
    @Transactional
    public int LikePost(Integer userId, Long postId) {
        PostLike postLike = new PostLike();
        postLike.setPostId(postId).setUserId(userId);
        return postLikeMapper.insert(postLike);
    }

    @Override
    public Integer hasLikePost(Integer userId, Long postId) {
        return postLikeMapper.hasLikePost(userId, postId);
    }

    @Override
    @Transactional
    public int removeLikePost(Integer userId, Long postId) {
        return postLikeMapper.removeLikePost(userId, postId);
    }
}
