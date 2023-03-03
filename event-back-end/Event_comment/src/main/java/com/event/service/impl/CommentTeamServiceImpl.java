package com.event.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.mapper.CommentTeamMapper;
import com.event.pojo.TeamComment;
import com.event.service.CommentTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CommentTeamServiceImpl extends ServiceImpl<CommentTeamMapper,TeamComment> implements CommentTeamService{

//    @Autowired
    private CommentTeamMapper mapper;
//
    @Override
    public boolean insertComment(TeamComment teamComment) {
        int insert = mapper.insert(teamComment);
        return insert>0?true:false;
    }
}
