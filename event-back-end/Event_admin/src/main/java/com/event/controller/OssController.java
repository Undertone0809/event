package com.event.controller;


import com.event.common.Result;
import com.event.common.ResultCodeEnum;
import com.event.utils.OssUtil;
import com.event.utils.SenInfoCheckUtil;
import com.event.utils.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss")
public class OssController{

    @Autowired
    private OssUtil ossUtil;

    @PostMapping("/uploadfile")
    public Result<String> fileUpload(@RequestParam("file") MultipartFile file, String fileName){
        try {
            if (!SenInfoCheckUtil.imgFilter(file)) return Result.error(ResultCodeEnum.TEXTERROR);
            String url = ossUtil.uploadFile(file,fileName+ Uuid.getUUID()); //调用OSS工具类
            if (url.equals("error")) return Result.error(ResultCodeEnum.ERROR);
            else return Result.ok_data(url,ResultCodeEnum.SUCCESS,1);
        }
        catch (Exception e) {e.printStackTrace();
            return  Result.error(ResultCodeEnum.SYSTEMERROR);
        }
    }
    @PostMapping("/study")
    public Result<String> study(@RequestParam("file") MultipartFile file,String fileName){
        try {
            if (!SenInfoCheckUtil.imgFilter(file))  return Result.error(ResultCodeEnum.TEXTERROR);
            String url = ossUtil.uploadFile(file,fileName); //调用OSS工具类
            if (url.equals("error")) return Result.error(ResultCodeEnum.ERROR);
            else return Result.ok_data(url,ResultCodeEnum.SUCCESS,1);
        }
        catch (Exception e) { e.printStackTrace();
            return  Result.error(ResultCodeEnum.SYSTEMERROR);
        }
    }
}
