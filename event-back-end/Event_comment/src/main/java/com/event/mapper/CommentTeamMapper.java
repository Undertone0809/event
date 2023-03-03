package com.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.event.pojo.TeamComment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommentTeamMapper extends BaseMapper<TeamComment> {


}
