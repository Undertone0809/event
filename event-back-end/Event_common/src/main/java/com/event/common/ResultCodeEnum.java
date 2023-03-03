package com.event.common;


import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    ERROR(false,10001,"失败"),
    SYSTEMERROR(false,10002,"系统异常"),
    MODIFYERROR(false,10003,"修改失败"),
    DELETEERROR(false,10007,"删除失败"),
    ADDERROR(false,10008,"添加失败"),
    NOTFOUNDERROR(false,10004,"查询为空"),
    USERNAMENOTFOUND(false,10005,"用户名不存在"),
    PASSWORDERROR(false,10006,"密码错误"),
    TEXTERROR(false,10007,"文本敏感"),
    IMGERROR(false,10008,"图片敏感"),
    NotONLINE(true,10009,"用户不在线"),
    YZMERROR(true,10010,"邮箱修改失败"),
    PASSWORDORUSERNAMEERROR(false,10011,"账号或密码错误"),
    EMAILERROR(false,10012,"该邮箱已被注册"),
    PARAMETERERROR(false,10012,"参数错误或为空"),
    TOKENINVALID(false,10013,"token失效"),
    SUCCESS(true,0,"成功");


    private final Boolean status; //响应是否成功
    private final Integer code; //返回码
    private final String msg; //返回消息

    ResultCodeEnum(Boolean status, Integer code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

}