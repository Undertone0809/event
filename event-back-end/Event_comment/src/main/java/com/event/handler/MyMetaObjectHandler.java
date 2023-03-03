package com.event.handler;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入时候的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");  //日志
        //设置字段的值（String fieldName字段名,Object fieldVal要传递的值,MetaObject metaObject)
        this.setFieldValByName("createTime",new Date(),metaObject);


        //this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        // this.fillStrategy(metaObject, "createTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug请升级到之后的版本如`3.3.1.8-SNAPSHOT`)
        /* 上面选其一使用,下面的已过时(注意 strictInsertFill 有多个方法,详细查看源码) */
        //this.setFieldValByName("operator", "Jerry", metaObject);
        //this.setInsertFieldValByName("operator", "Jerry", metaObject);
    }

    //更新时间的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
    }

}
