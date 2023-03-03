package com.event.mapper;

import com.event.pojo.Contest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author hanshuheng
* @description 针对表【contest】的数据库操作Mapper
* @createDate 2022-05-18 00:19:50
* @Entity com.event.pojo.Contest
*/
@Mapper
@Repository
public interface ContestMapper extends BaseMapper<Contest> {

}




