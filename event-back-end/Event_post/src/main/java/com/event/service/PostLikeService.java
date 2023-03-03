package com.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.event.pojo.PostCollect;
import com.event.pojo.PostLike;

public interface PostLikeService   extends IService<PostLike> {

    Integer beSelectLikePostNum(Long postId);

    int LikePost(Integer userId,Long postId);

    Integer hasLikePost(Integer userId,Long postId);

    int removeLikePost(Integer userId,Long postId);
}
