package com.event.controller;


import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.feign.Comment_postFeign;
import com.event.pojo.Post;
import com.event.pojo.PostComment;
import com.event.service.CommentLikeService;
import com.event.service.CommentService;
import com.event.utils.GetIp;
import com.event.utils.SenInfoCheckUtil;
import org.apache.coyote.OutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 帖子一级评论 前端控制器
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-01
 */
@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentController {
    @Autowired
    private CommentService commentPostService;

    @Autowired
    private Comment_postFeign commentPostFeign;
    @Autowired
    private CommentLikeService commentLikeService;



    @GetMapping("/selectComment")
    public Result<List<PostComment>> selectComment(PostComment comment) {
       List<PostComment> comments = commentPostService.selectPostComment(comment);
       return comments.size()==0 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(comments, ResultCodeEnum.SUCCESS, comments.size());
    }

    @GetMapping("/selectCommentByLike")
    public Result<List<PostComment>> selectCommentByLike(PostComment comment) {
       List<PostComment> comments = commentPostService.selectPostCommentByLike(comment);
       return comments.size()==0 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(comments, ResultCodeEnum.SUCCESS, comments.size());
    }

    // 评论喜欢加一
    @PostMapping("/postCommentLikeAdd1/{commentId}")
    public Result<Boolean> postCommentLikeAdd1(@PathVariable Long commentId){
        if (commentPostService.postCommentLikeAdd1(commentId) > 0) return Result.ok_data(true,ResultCodeEnum.SUCCESS,1);
        else return Result.error(ResultCodeEnum.MODIFYERROR);
    }

    // 增加评论
    @PostMapping("/insertComment")
    public Result<Void> insertComment(@RequestBody PostComment comment, HttpServletRequest request) throws Exception {
        if (!SenInfoCheckUtil.cotentFilter(comment.getCommentContent())) return Result.error(ResultCodeEnum.TEXTERROR);
        GetIp getIp = new GetIp(request);
        comment.setCommentIp(getIp.getIpAddress()).setCommendLikes(0L);
        if(commentPostService.insertComment(comment)>0){
            Post post = commentPostFeign.selectPostById(comment.getPostId());
            post.setPostCommentNum(post.getPostCommentNum()+1);
            if (commentPostService.addCommentNum(comment.getPostId())>0){
                return Result.ok();
            }
            else {
                return Result.error(ResultCodeEnum.ADDERROR);
            }
        }else return Result.error(ResultCodeEnum.ADDERROR);
    }

    // 删除评论
    @PostMapping("/deleteComment/{commentId}")
    public Result<Void> deleteComment(@PathVariable Long commentId) {
        // 获取帖子id
        Long postId = commentPostService.selectCommentById(commentId).getPostId();
        // 删除评论后，帖子评论数减一
        if(commentPostService.deleteComment(commentId) &&
            Objects.equals(commentPostFeign.minusCommentNum(postId).getCode(), ResultCodeEnum.SUCCESS.getCode())){
            return Result.ok();
        }
        return Result.error(ResultCodeEnum.DELETEERROR);
    }

    @PostMapping("/likeComment/{commentId}")
    public Result<Void> likeComment(@PathVariable Long commentId) {
        if(commentPostService.likeComment(commentId)>0) return Result.ok();
        return Result.error(ResultCodeEnum.MODIFYERROR);
    }

    /* 传入帖子id查询所有一级评论 */
    @GetMapping("/selectTopComment/{postId}")
    public Result<List<PostComment>> selectTopComment(@PathVariable Long postId) {
        List<PostComment> comments = commentPostService.selectTopComment(postId);
        return comments.size()==0 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(comments, ResultCodeEnum.SUCCESS, comments.size());
    }

    /* 传入一级评论的id查询所有的子评论 */
    @GetMapping("/selectSubComment/{faId}")
    public Result<List<PostComment>> selectSubComment(@PathVariable Long faId) {
        List<PostComment> comments = commentPostService.selectSubComment(faId);
        return comments.size()==0 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(comments, ResultCodeEnum.SUCCESS, comments.size());
    }

    /* 传入一级评论的id查询最多2个子评论 */
    @GetMapping("selectSubLE2/{faId}")
    public Result<List<PostComment>> selectSubLE2(@PathVariable Long faId) {
        List<PostComment> comments = commentPostService.selectSubLE2(faId);
        return comments.size()==0 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(comments, ResultCodeEnum.SUCCESS, comments.size());
    }


    @PostMapping("/likePostComment/{userId}/{commentId}")
    public Result<Void> likePostComment(@PathVariable Integer userId,@PathVariable Long commentId){
        if (commentLikeService.LikePostComment(userId, commentId) > 0) return Result.ok();
        else return Result.error(ResultCodeEnum.ADDERROR);
    }
    @PostMapping("/removeLikePostComment/{userId}/{commentId}")
    public Result<Void> removeLikePostComment(@PathVariable Integer userId,@PathVariable Long commentId){
        if (commentLikeService.removeLikePostComment(userId, commentId) > 0) return Result.ok();
        else return Result.error(ResultCodeEnum.DELETEERROR);
    }
    @GetMapping("/hasLikePostComment/{userId}/{commentId}")
    public Result<Boolean> hasLikePostComment(@PathVariable Integer userId,@PathVariable Long commentId){
        return Result.ok_data(commentLikeService.hasLikePostCommentLike(userId, commentId) != null,ResultCodeEnum.SUCCESS,1);
    }
    @GetMapping("/beSelectLikePostCommentNum/{commentId}")
    public Result<Integer> beSelectLikePostCommentNum(@PathVariable Long commentId){
        return Result.ok_data(commentLikeService.beSelectPostCommentLikeNum(commentId),ResultCodeEnum.SUCCESS
                ,commentLikeService.beSelectPostCommentLikeNum(commentId));
    }
}
