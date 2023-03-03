package com.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.pojo.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PostLikeMapper  extends BaseMapper<PostLike> {

    Integer beSelectLikePostNum(@Param("postId") Long postId);

    Integer hasLikePost(@Param("userId") Integer userId,@Param("postId") Long postId);

    int removeLikePost(@Param("userId") Integer userId,@Param("postId") Long postId);
}
