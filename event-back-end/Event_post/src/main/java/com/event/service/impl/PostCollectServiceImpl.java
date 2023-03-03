package com.event.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.PostCollectMapper;
import com.event.pojo.Post;
import com.event.pojo.PostCollect;
import com.event.service.PostCollectService;
import com.event.vo.TeamInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostCollectServiceImpl extends ServiceImpl<PostCollectMapper, PostCollect> implements PostCollectService {

    @Autowired
   PostCollectMapper postCollectMapper;

    @Override
    public Integer beSelectCollectionPostNum(Long postId) {
        return postCollectMapper.beSelectCollectionPostNum(postId);
    }

    @Override
    public List<Post> selectCollectionPost(Long userId) {
        return postCollectMapper.selectCollectionPost(userId);
    }

    @Override
    @Transactional
    public int collectionPost(Integer userId, Long postId) {
        PostCollect postCollect = new PostCollect();
        postCollect.setPostId(postId).setUserId(userId);
        return postCollectMapper.insert(postCollect);
    }

    @Override
    public Integer hasCollectionPost(Integer userId, Long postId) {
        return postCollectMapper.hasCollectionPost(userId, postId);
    }

    @Override
    @Transactional
    public int removeCollectionPost(Integer userId, Long postId) {
        return postCollectMapper.removeCollectionPost(userId, postId);
    }

    @Override
    public Page<Post> selectCollectionPostAndPage(Page page, Long userId) {
        return postCollectMapper.selectCollectionPostAndPage(page,userId);
    }
}
