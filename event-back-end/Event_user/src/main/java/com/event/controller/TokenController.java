package com.event.controller;

import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Stack;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("all")
@RestController
@RequestMapping("/token")
@CrossOrigin(origins = "*",maxAge = 3600)
public class TokenController {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *
     * @return 登录成功后调用获取token储存到本地cookie
     */
    @PostMapping("/createToken/{userid}")
    public Result<String> createToken(@PathVariable("userid") Integer userid){
        String userId = String.valueOf(userid);
        String token = JwtToken.createToken(String.valueOf(userid));
        redisTemplate.opsForValue().set(token, userId, 7, TimeUnit.DAYS);
        return Result.ok_data(token, ResultCodeEnum.SUCCESS,1);
    }

    /**
     *
     * @return 判断token是否有效,若无效则用户处于未登录状态
     */
    @PostMapping("/isToken/{token}")
    public Result<Boolean> isToken(@PathVariable("token") String token){
        try {
            //客户端令牌过期，查询Redis中是否存在令牌，如果存在令牌就重新生成一个令牌给客户端
            if (redisTemplate.hasKey(token)) {
                String userId = (String) redisTemplate.opsForValue().get(token);
                redisTemplate.delete(token);
                token = JwtToken.createToken(userId);  //生成新的令牌
                redisTemplate.opsForValue().set(token, userId, 7, TimeUnit.DAYS);//把新的令牌保存到Redis中
            } else return Result.ok_data(false,ResultCodeEnum.TOKENINVALID,1);

        } catch (Exception e) {
            return Result.ok_data(false,ResultCodeEnum.TOKENINVALID,1);
        }
        return Result.ok_data(true,ResultCodeEnum.SUCCESS,1);
    }
}
