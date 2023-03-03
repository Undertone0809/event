package com.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.CommentTeamMapper;
import com.event.pojo.PostCommentLike;
import com.event.pojo.TeamComment;

public interface CommentTeamService extends IService<TeamComment>{

    // 插入数据
    boolean insertComment(TeamComment teamComment);

}
