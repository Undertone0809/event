package com.event.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.ContestPostMapper;
import com.event.pojo.ContestPost;
import com.event.service.ContestPostService;
import com.event.vo.PostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86132
* @description 针对表【contest_post】的数据库操作Service实现
* @createDate 2022-07-30 03:25:05
*/
@Service
public class ContestPostServiceImpl extends ServiceImpl<ContestPostMapper, ContestPost>
    implements ContestPostService {

    @Autowired
    private ContestPostMapper contestPostMapper;

    @Override
    public List<PostDetailVo> selectContestPostByPage(Long contestId, Integer pageNum, Integer pageSize) {
        return contestPostMapper.selectContestPostByPage(contestId,(pageNum-1)*pageSize,pageSize);
    }
}




