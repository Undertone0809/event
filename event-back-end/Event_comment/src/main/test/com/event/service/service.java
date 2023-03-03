package com.event.service;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.event.common.Result;
import com.event.feign.Comment_postFeign;
import com.event.pojo.Post;
import com.event.pojo.TeamComment;
import com.event.service.impl.CommentTeamServiceImpl;
import com.event.service.impl.PostServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class service {

    @Qualifier("commentTeamServiceImpl")
    @Autowired
    private CommentTeamServiceImpl commentTeamService;

//    @Qualifier("commentPostFeign")
    @Autowired
    private Comment_postFeign commentPostFeign;

    @Autowired
    private PostService postService;

    @Test
    public void testSerTeam(){
        TeamComment teamComment = new TeamComment();

        teamComment.setCommentIp("120.12.32.123");
        teamComment.setCommentContent("你好");
        teamComment.setTeamId(1492720673280290818L);
        teamComment.setUserId(27);

        if (commentTeamService==null){
            System.out.println("null");
        }

        boolean save = commentTeamService.insertComment(teamComment);

        if (save){
            System.out.println("保存成功");
        }else{
            System.out.println("保存失败");
        }
    }



    @Test
    public void testPage(){

        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", 1498608509275713538L);

        System.out.println("postService==>"+postService);

//        Post post = postService.selectPostById(1498608509275713538L);
//        System.out.println(post);
    }

    @Test
    public void testCom(){
        Post post = new Post();
        post.setPostId(1526505564908044292L);
        post.setUserId(1);
        post.setPostTitle("sdfs");
        post.setPostContent("sdlkfjs");
        post.setPostType("bb");

        Result<Void> voidResult = commentPostFeign.updatePost(post);
        System.out.println(voidResult);


    }

}
