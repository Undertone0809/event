package com.event.feign;

import com.event.common.Result;
import com.event.pojo.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


@FeignClient("post")
public interface Comment_postFeign {
    @GetMapping("/post/selectPostById")
    Post selectPostById(@RequestParam Long postId);
    @PostMapping("/post/updatePost")
    Result<Void> updatePost(@RequestBody Post post);
    @PostMapping("/post/minusPostCommentNum/{postId}")
    Result<Post> minusCommentNum(@PathVariable("postId") Long postId);

}
