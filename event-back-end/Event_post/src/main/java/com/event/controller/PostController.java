package com.event.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.form.SearchPostForm;
import com.event.pojo.Post;
import com.event.service.PostCollectService;
import com.event.service.PostLikeService;
import com.event.service.PostService;
import com.event.utils.GetIp;
import com.event.utils.SenInfoCheckUtil;
import com.event.vo.PostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 帖子 前端控制器
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-01
 */
@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "*",maxAge = 3600)
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostCollectService postCollectService;


    /**
     * 根据关键词搜索帖子
     */
    @PostMapping("/searchPost")
    public Result<List<PostDetailVo>> searchPost(@RequestBody SearchPostForm searchPostForm){
        List<PostDetailVo> list = postService.searchPostByContentAndPage(searchPostForm);
        return Result.ok_data(list,ResultCodeEnum.SUCCESS,list.size());
    }

    /**
     * 按照浏览量分页查询帖子
     */
    @GetMapping("/selectPostByViews/{pageNum}/{pageSize}")
    public Result<List<PostDetailVo>> selectPostByViews(@PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize){
        List<PostDetailVo> list = postService.selectPostByViews(pageNum,pageSize);
        return Result.ok_data(list,ResultCodeEnum.SUCCESS,list.size());
    }

    /**
     * 按照用户id查询帖子具体信息
     */
    @GetMapping("/selectPostByUserId/{userId}/{pageNum}/{pageSize}")
    public Result<List<PostDetailVo>> selectPostByUserId(@PathVariable String userId,@PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize){
        List<PostDetailVo> list = postService.selectPostByUserId(userId,pageNum,pageSize);
        return Result.ok_data(list,ResultCodeEnum.SUCCESS,list.size());
    }


    /**
     * 查询我关注的人发布的帖子
     */
    @GetMapping("/selectMyFriendPost/{userId}/{pageSize}/{pageNum}")
    public Result<List<PostDetailVo>> selectMyFriendPost(@PathVariable("userId") String userId,
             @PathVariable("pageSize") Integer pageSize, @PathVariable("pageNum") Integer pageNum){
        List<PostDetailVo> list = postService.selectMyFriendPost(userId,pageSize,pageNum);
        return Result.ok_data(list,ResultCodeEnum.SUCCESS,list.size());
    }

    // 通过postId查询帖子
    @GetMapping("/selectPostById")
    public Post selectPostById(Long postId){
        postService.addPostView(postId);
        return postService.selectPostById(postId);
    }

    // 更新帖子
    @PostMapping("/updatePost")
    public Result<Void> updatePost(@RequestBody Post post){
        if (postService.updatePost(post)>0) return Result.ok();
      else {
          return Result.error(ResultCodeEnum.MODIFYERROR);
        }
    }

    // 评论数减1
    @PostMapping("/minusPostCommentNum/{postId}")
    public Result<Post> minusCommentNum(@PathVariable("postId") Long postId){
        Post post = postService.selectPostById(postId);
        post.setPostCommentNum(post.getPostCommentNum()-1);
        if(postService.updatePost(post)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.MODIFYERROR);
    }

    @GetMapping("/selectPost")
    public Result<List<Post>> selectPost(Post post){
        List<Post> posts = postService.selectPost(post);
        postService.addPostView(post.getPostId());
        return posts.size() < 1 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(posts,ResultCodeEnum.SUCCESS,posts.size());
    }

    @Deprecated
    @GetMapping("/selectPostByView")
    public Result<List<Post>> selectPostByView(Post post){
        List<Post> posts = postService.selectPostByView(post,"评论数");
        return posts.size() < 1 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(posts,ResultCodeEnum.SUCCESS,posts.size());
    }

    @GetMapping("/selectPostByUptime")
    public Result<List<Post>> selectPostByUptime(Post post){
        List<Post> posts = postService.selectPostByUptime(post,"更新时间");
        return posts.size() < 1 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(posts,ResultCodeEnum.SUCCESS,posts.size());
    }
    @GetMapping("/selectPostByPostTitle")
    public Result<List<Post>> selectPostByPostTitle(String postTitle){
        List<Post> posts = postService.selectPostByPostTitle(postTitle,"标题");
        return posts.size() < 1 ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(posts,ResultCodeEnum.SUCCESS,posts.size());
    }
    // 通过postId删除post
    @PostMapping("/deletePost/{postId}")
    @CacheEvict(value = "PostComment", allEntries=true)
    public Result<Void> deletePost(@PathVariable Long postId){
        if (!postService.deletePost(postId)) return Result.error(ResultCodeEnum.DELETEERROR);
        else return Result.ok();
    }
    // 增加一条post
    @PostMapping("/insertPost")
    public Result<Post> insertPost(@RequestBody Post post, HttpServletRequest request) throws Exception {
        GetIp getIp = new GetIp(request);
        post.setPostIp(getIp.getIpAddress()).setPostViews((long)0).setPostCommentNum((long)0);
        if (SenInfoCheckUtil.cotentFilter(post.getPostTitle())&&SenInfoCheckUtil.cotentFilter(post.getPostContent()))
            if (postService.save(post)) return Result.ok_data(post,ResultCodeEnum.SUCCESS,1);
            else return Result.error(ResultCodeEnum.ADDERROR);
        else {
            return Result.error(ResultCodeEnum.TEXTERROR);
        }
    }
    @PostMapping("/viewPost")
    public Result<Void> viewPost(@RequestBody Post post){
        List<Post> posts = postService.selectPost(post);
        Post post1 = posts.get(0);
        post1.setPostViews(post1.getPostViews()+1);
        if (postService.updatePost(post1)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.MODIFYERROR);
    }
    // 收藏post
    @PostMapping("/collectionPost/{userId}/{postId}")
    public Result<Void> collectionPost(@PathVariable Integer userId,@PathVariable Long postId){
        if (postCollectService.collectionPost(userId,postId)>0){
            Post post = postService.selectPostById(postId);
            post.setPostCollectNum(post.getPostCollectNum() + 1);
            if (postService.updatePost(post) > 0) return Result.ok();
        }
        return Result.error(ResultCodeEnum.ADDERROR);
    }
    // 删除post
    @PostMapping("/removeCollectionPost/{userId}/{postId}")
    public Result<Void> removeCollectionPost(@PathVariable Integer userId,@PathVariable Long postId){
        if (postCollectService.removeCollectionPost(userId,postId) > 0) return Result.ok();
        else return Result.error(ResultCodeEnum.DELETEERROR);
    }
    // 通过userId 查看所有的收藏post
    @GetMapping("/selectCollectionPost/{userId}")
    public Result<List<Post>> selectCollectionPost(@PathVariable Long userId){
        List<Post> posts = postCollectService.selectCollectionPost(userId);
        return Result.ok_data(posts,ResultCodeEnum.SUCCESS,posts.size());
    }

    @GetMapping("/hasCollectionPost/{userId}/{postId}")
    public Result<Boolean> hasCollectionPost(@PathVariable Integer userId,@PathVariable Long postId){
        return Result.ok_data(postCollectService.hasCollectionPost(userId, postId) != null,ResultCodeEnum.SUCCESS,1);
    }
    @GetMapping("/beSelectCollectionPostNum/{postId}")
    public Result<Integer> beSelectCollectionTeamNum(@PathVariable("postId")Long postId){
        return Result.ok_data(postCollectService.beSelectCollectionPostNum(postId),ResultCodeEnum.SUCCESS
                ,postCollectService.beSelectCollectionPostNum(postId));
    }
    // 点赞post
    @PostMapping("/likePost/{userId}/{postId}")
    public Result<Void> likePost(@PathVariable Integer userId,@PathVariable Long postId){
        if (postLikeService.LikePost(userId, postId) > 0) {
            Post post = postService.selectPostById(postId);
            post.setPostLikeNum(post.getPostLikeNum() + 1);
            if (postService.updatePost(post) > 0)
            return Result.ok();
        }
        return Result.error(ResultCodeEnum.ADDERROR);
    }
    // 取消点赞post
    @PostMapping("/removeLikePost/{userId}/{postId}")
    public Result<Void> removeLikePost(@PathVariable Integer userId,@PathVariable Long postId){
        if (postLikeService.removeLikePost(userId, postId) > 0){
            Post post = postService.selectPostById(postId);
            System.out.println(post);
            post.setPostLikeNum(post.getPostLikeNum() - 1);
            System.out.println(post);
            if (postService.updatePost(post) > 0)
            {
                System.out.println(post);
                return Result.ok();
            }
        }
        return Result.error(ResultCodeEnum.DELETEERROR);
    }
    @GetMapping("/hasLikePost/{userId}/{postId}")
    public Result<Boolean> hasLikePost(@PathVariable Integer userId,@PathVariable Long postId){
        return Result.ok_data(postLikeService.hasLikePost(userId, postId) != null,ResultCodeEnum.SUCCESS,1);
    }
    @GetMapping("/beSelectLikePostNum/{postId}")
    public Result<Integer> beSelectLikePostNum(@PathVariable("postId")Long postId){
        return Result.ok_data(postLikeService.beSelectLikePostNum(postId),ResultCodeEnum.SUCCESS
                ,postLikeService.beSelectLikePostNum(postId));
    }

    /**
     * 接收userid  返回用户发的帖子  分页
     */
    @Deprecated
    @GetMapping("/selectAllPostByUserid/{userId}/{number}/{pageSize}")
    public Result<List<Post>> selectAllPostByUserid(@PathVariable("userId") Long userid,@PathVariable("pageSize") int pageSize,@PathVariable("number") int number){

        int current = (number-1)*pageSize;
        Page<Post> page = new Page<>(current, pageSize);
        Page<Post> postPage = postCollectService.selectCollectionPostAndPage(page, userid);
        if (postPage==null){
            return Result.error("该用户没有发帖子");
        }
        List<Post> postList = postPage.getRecords();
        int allPage = (int) postPage.getPages();
        return Result.ok_data(postList,ResultCodeEnum.SUCCESS,allPage);

    }
}
