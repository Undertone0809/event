package com.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.pojo.PostCommentLike;
import com.event.pojo.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommentLikeMapper extends BaseMapper<PostCommentLike> {


    Integer beSelectPostCommentLikeNum(@Param("commentId") Long commentId);

    Integer hasLikePostCommentLike(@Param("userId") Integer userId,@Param("commentId") Long commentId);

    int removeLikePostComment(@Param("userId") Integer userId,@Param("commentId") Long commentId);
}
