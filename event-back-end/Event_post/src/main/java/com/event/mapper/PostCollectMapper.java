package com.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.event.pojo.Post;
import com.event.pojo.PostCollect;
import com.event.vo.TeamInfo;
import javafx.geometry.Pos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PostCollectMapper  extends BaseMapper<PostCollect> {
    Integer beSelectCollectionPostNum(@Param("postId") Long postId);

    List<Post> selectCollectionPost(@Param("userId") Long userId);

    /**
     * 分页 通过用户id 查询其所有post
     * @param userId
     * @param page
     * @return
     */
    Page<Post> selectCollectionPostAndPage(@Param("page") Page page, @Param("userId") Long userId);

    Integer hasCollectionPost(@Param("userId") Integer userId,@Param("postId") Long postId);

    int removeCollectionPost(@Param("userId") Integer userId,@Param("postId") Long postId);


}
