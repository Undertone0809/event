package com.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.event.pojo.PostComment;
import com.event.mapper.CommentMapper;
import com.event.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 帖子一级评论 服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-01
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, PostComment> implements CommentService {
    @Autowired
    CommentMapper mapper;

    @Override
    @Cacheable(value = "PostComment")
    @Transactional
    public int postCommentLikeAdd1(Long commentId) {
        return mapper.postCommentLikeAdd1(commentId);
    }

    @Override
    @Cacheable(value = "PostComment")
    public List<PostComment> selectPostComment(PostComment postComment) {
        return mapper.selectPostComment(postComment);
    }

    @Override
    public List<PostComment> selectPostCommentByLike(PostComment postComment) {
        return mapper.selectPostCommentByLike(postComment);
    }

    @Override
    @CacheEvict(value = "PostComment", allEntries = true)
    @Transactional
    public boolean deleteComment(Long commentId) {
        return removeById(commentId);
    }

    @Override
    @CacheEvict(value = "PostComment", allEntries = true)
    @Transactional
    public int insertComment(PostComment postComment) {
        return mapper.insert(postComment);
    }

    @Override
    public int likeComment(Long commentId) {
        UpdateWrapper<PostComment> wrapper = new UpdateWrapper<>();
        wrapper.eq("comment_id", commentId).setSql("commend_likes=commend_likes+1");
        return mapper.update(null, wrapper);
    }

    @Override
    public List<PostComment> selectTopComment(Long postId) { //所有一级评论
        PostComment pc = new PostComment();
        pc.setPostId(postId);
        return mapper.selectTopComment(pc);
    }

    @Override
    public List<PostComment> selectSubComment(Long fa) { //一个一级评论下的所有二级评论
        PostComment faComment = new PostComment(); faComment.setCommentId(fa);
        faComment = mapper.selectPostComment(faComment).get(0);
        PostComment finalFaComment = faComment;

        PostComment pc = new PostComment(); pc.setFaCommentId(fa);
        List<PostComment> init = mapper.selectPostComment(pc); //直接回复该评论的评论数组

        // init = init.parallelStream().peek(t -> t.setReplyName(finalFaComment.getOpenname())).collect(Collectors.toList());

        List<PostComment> rs = new ArrayList<>(); //暂存出队元素
        Queue<PostComment> queue = new LinkedList<>(init);
        while (!queue.isEmpty()) {
            PostComment head = queue.poll();  rs.add(head);
            PostComment t = new PostComment();
            t.setFaCommentId(head.getCommentId());
            List<PostComment> tlist = mapper.selectPostComment(t);
            for (PostComment x : tlist)  {
                x.setReplyName(head.getOpenname());
                queue.offer(x);
            }
        }
        return rs;
    }

    @Override
    public List<PostComment> selectSubLE2(Long fa) { //一个一级评论下的最多2条二级评论
        List<PostComment> rs = new ArrayList<>();

        PostComment pc = new PostComment();
        pc.setFaCommentId(fa);
        pc.setIdx(0);
        List<PostComment> init = mapper.selectPostComment(pc); //直接回复该评论的评论数组
        if(init.size()!=1) return init;

        PostComment theOne = init.get(0);
        rs.add(theOne);

        PostComment t = new PostComment();
        t.setFaCommentId(theOne.getCommentId());
        t.setIdx(0);
        List<PostComment> tlist = mapper.selectPostComment(t);
        if(tlist.size()>0) {
            PostComment theTwo = tlist.get(0);
            theTwo.setReplyName(theOne.getOpenname());
            rs.add(tlist.get(0));
        }

        return rs;
    }

    @Override
    public int addCommentNum(Long postId) {
        return mapper.addCommentNum(postId);
    }

    @Override
    public PostComment selectCommentById(Long commentId) {
        return mapper.selectCommentById(commentId);
    }


}
