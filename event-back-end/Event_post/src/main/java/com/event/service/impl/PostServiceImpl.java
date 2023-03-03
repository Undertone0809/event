package com.event.service.impl;

import com.event.form.SearchPostForm;
import com.event.pojo.Post;
import com.event.mapper.PostMapper;
import com.event.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.vo.PostDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 帖子 服务实现类
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-01
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Autowired
    PostMapper postMapper;

    @Override
    @Cacheable("Post")
    public Post selectPostById(Long postId) {
        return postMapper.selectPostById(postId);
    }

    @Override
    @Cacheable("Post")
    public List<Post> selectPost(Post post){
        return postMapper.selectPost(post);
    }

    @Override
    @Cacheable("Post")
    public List<Post> selectPostByView(Post post,String flag) {
        return postMapper.selectPostByView(post);
    }

    @Override
    @CacheEvict(value = "Post", allEntries=true)
    @Transactional
    public boolean deletePost(Long postId) {
        return removeById(postId);
    }

    @Override
    @CacheEvict(value = "Post", allEntries=true)
    @Transactional
    public int insertPost(Post post) {
        return postMapper.insert(post);
    }

    @Override
    @CacheEvict(value = "Post", allEntries=true)
    @Transactional
    public int updatePost(Post post) {
        return postMapper.updatePost(post);
    }

    @Override
    public int addPostView(Long postId) {
        return postMapper.addPostView(postId);
    }

    @Override
    @Cacheable("Post")
    public List<Post> selectPostByPostTitle(String postTitle,String flag) {
        return postMapper.selectPostByPostTitle(postTitle);
    }

    @Override
    @Cacheable("Post")
    public List<Post> selectPostByUptime(Post post,String flag) {
        return postMapper.selectPostByUptime(post);
    }

    @Override
    @Cacheable("Post")
    public List<PostDetailVo> searchPostByContentAndPage(SearchPostForm searchPostForm) {
        String keyword = searchPostForm.getKeyword();
        Integer pageNum = searchPostForm.getPageNum();
        Integer pageSize = searchPostForm.getPageSize();
        return postMapper.searchPostByContentAndPage(keyword,(pageNum-1)*pageSize,pageSize);
    }

    @Override
    @Cacheable("Post")
    public List<PostDetailVo> selectMyFriendPost(String userId, Integer pageNum, Integer pageSize){
        return postMapper.selectMyFriendPost(userId,(pageNum-1)*pageSize,pageSize);
    }

    @Override
    public List<PostDetailVo> selectPostByViews(Integer pageNum, Integer pageSize) {
        return postMapper.selectPostByViews((pageNum-1)*pageSize,pageSize);
    }

    @Override
    public List<PostDetailVo> selectPostByUserId(String userId, Integer pageNum, Integer pageSize) {
        return postMapper.selectPostByUserId(userId,(pageNum-1)*pageSize,pageSize);
    }

}
