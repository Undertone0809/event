package com.event.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.event.pojo.Contest;
import com.event.service.ContestService;
import com.event.mapper.ContestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hanshuheng
* @description 针对表【contest】的数据库操作Service实现
* @createDate 2022-05-18 00:19:50
*/
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest>
    implements ContestService{

}




