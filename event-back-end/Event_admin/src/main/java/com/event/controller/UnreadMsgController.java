package com.event.controller;


import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 未读消息 前端控制器
 * </p>
 *
 * @author 孤雏
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/unreadMsg")
@CrossOrigin(origins = "*",maxAge = 3600)
public class UnreadMsgController {
    @Autowired
    private  RedisTemplate redisTemplate;
    @GetMapping("/selectUnreadMsg/{userid}")
    public Result<Map<String,Integer>> selectUnreadMsg(@PathVariable("userid") Integer userid){
        Map<String, Integer> map = new ConcurrentHashMap<>();
        Integer unreadMsg;
        try {
            unreadMsg = (Integer) redisTemplate.opsForHash().get("UnreadMsg", userid.toString());
            if (unreadMsg==null||unreadMsg==0) return Result.error(ResultCodeEnum.NOTFOUNDERROR);
            map.put("count",unreadMsg);
            return Result.ok_data(map,ResultCodeEnum.SUCCESS,map.size());
        } catch (Exception e) { e.printStackTrace();
            return  Result.error(ResultCodeEnum.SYSTEMERROR);
        }
    }
    @PostMapping("/clearUnreadMsg/{userid}")
    public Result<Void> clearUnreadMsg(@PathVariable("userid")Integer userid){
        try {
            redisTemplate.opsForHash().put("UnreadMsg",userid.toString(),0);
           return Result.ok();
        } catch (Exception e) {e.printStackTrace();
            return  Result.error(ResultCodeEnum.SYSTEMERROR);
        }
    }
}
