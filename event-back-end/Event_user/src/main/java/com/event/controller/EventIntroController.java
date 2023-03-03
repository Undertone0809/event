package com.event.controller;


import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.pojo.EventIntro;
import com.event.service.EventIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 一问用户简介表 前端控制器
 * </p>
 *
 * @author 孤雏
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/intro")
@CrossOrigin(origins = "*",maxAge = 3600)
public class EventIntroController {

    @Autowired
    private EventIntroService eventIntroService;

    @GetMapping("/selectIntroById/{userId}")
    public Result<EventIntro> selectIntroById(@PathVariable("userId") Integer userId){
        EventIntro eventIntro = eventIntroService.selectIntroById(userId);
        return Result.ok_data(eventIntro, ResultCodeEnum.SUCCESS,1);
    }
    @PostMapping("/updateIntroById")
    public Result<Void> updateIntroById(@RequestBody EventIntro eventIntro){
        if (eventIntroService.updateIntro(eventIntro)>0) return Result.ok();
        else return Result.error(ResultCodeEnum.MODIFYERROR);
    }
}
