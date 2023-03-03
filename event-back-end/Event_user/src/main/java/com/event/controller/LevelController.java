package com.event.controller;

import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.pojo.EventLevel;
import com.event.service.EventLevelService;
import com.event.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户经验信息
 * </p>
 *
 * @author 孤雏
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/level")
@CrossOrigin(origins = "*",maxAge = 3600)
public class LevelController {
    @Autowired
    private EventLevelService levelService;
    /**
     *
     * @return 根据条件查询用户等级
     */
    @GetMapping("/selectLevel")
    public Result<List<EventLevel>> selectLevel(EventLevel level, Integer row){
        List<EventLevel> users;
        try {
            users = levelService.selectLevel(level,row);
        } catch (Exception e) {e.printStackTrace();
            return Result.error(ResultCodeEnum.SYSTEMERROR);
        }
        return users == null ? Result.error(ResultCodeEnum.NOTFOUNDERROR) : Result.ok_data(users,ResultCodeEnum.SUCCESS,users.size());
    }
    /**
     *
     * @return 根据条件更新用户等级信息
     */
    @PostMapping("/updateLevel")
    public Result<Void> updateLevel(@RequestBody EventLevel level){
        try {
            if (levelService.updateLevel(level)>0) return Result.ok();
            else return Result.error(ResultCodeEnum.MODIFYERROR);
        } catch (Exception e) { e.printStackTrace();
            return Result.error(ResultCodeEnum.SYSTEMERROR);
        }
    }
}
