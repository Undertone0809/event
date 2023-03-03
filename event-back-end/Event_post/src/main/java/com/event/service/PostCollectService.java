package com.event.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.event.pojo.Post;
import com.event.pojo.PostCollect;
import com.event.vo.TeamInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostCollectService  extends IService<PostCollect> {

    Integer beSelectCollectionPostNum(Long postId);

    List<Post> selectCollectionPost(Long userId);

    int collectionPost(Integer userId,Long postId);

    Integer hasCollectionPost(Integer userId,Long postId);

    int removeCollectionPost(Integer userId,Long postId);

    Page<Post> selectCollectionPostAndPage(Page page,Long userId);

}

