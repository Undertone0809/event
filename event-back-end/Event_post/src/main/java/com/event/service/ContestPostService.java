package com.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.event.pojo.ContestPost;
import com.event.vo.PostDetailVo;

import java.util.List;

/**
* @author 86132
* @description 针对表【contest_post】的数据库操作Service
* @createDate 2022-07-30 03:25:05
*/
public interface ContestPostService extends IService<ContestPost> {

    List<PostDetailVo> selectContestPostByPage(Long contestId, Integer pageNum, Integer pageSize);
}
