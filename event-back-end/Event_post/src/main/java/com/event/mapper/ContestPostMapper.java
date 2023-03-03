package com.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.pojo.ContestPost;
import com.event.vo.PostDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
* @author 86132
* @description 针对表【contest_post】的数据库操作Mapper
* @createDate 2022-07-30 03:25:05
* @Entity generator.domain.ContestPost
*/
@Mapper
@Repository
public interface ContestPostMapper extends BaseMapper<ContestPost> {

    List<PostDetailVo> selectContestPostByPage(Long contestId, Integer offset, Integer pageSize);
}




