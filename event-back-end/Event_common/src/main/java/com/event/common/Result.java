package com.event.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result<T> implements Serializable {
    private Boolean success;

    private Integer code;

    private String message;

    private  T data;

    private Integer count;

    public Result(){

    }

    public static <T> Result<T> ok(){
        Result<T> r = new Result<>();
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setSuccess(ResultCodeEnum.SUCCESS.getStatus());
        r.setMessage(ResultCodeEnum.SUCCESS.getMsg());
        return r;
    }

    public static <T> Result<T> error(){
        Result<T> r = new Result<>();
        r.setCode(ResultCodeEnum.ERROR.getCode());
        r.setSuccess(ResultCodeEnum.ERROR.getStatus());
        r.setMessage(ResultCodeEnum.ERROR.getMsg());
        return r;
    }

    public static <T> Result<T> error(String msg){
        Result<T> r = new Result<>();
        r.setCode(ResultCodeEnum.ERROR.getCode());
        r.setSuccess(ResultCodeEnum.ERROR.getStatus());
        r.setMessage(msg);
        return r;
    }

    public static <T> Result<T> ok(ResultCodeEnum codeEnum){
        Result<T> r = new Result<>();
        r.setCode(codeEnum.getCode());
        r.setSuccess(codeEnum.getStatus());
        r.setMessage(codeEnum.getMsg());
        return r;
    }

    public static <T> Result<T> error(ResultCodeEnum codeEnum){
        Result<T> r = new Result<>();
        r.setCode(codeEnum.getCode());
        r.setSuccess(codeEnum.getStatus());
        r.setMessage(codeEnum.getMsg());
        return r;
    }
    public static <T> Result<T> ok_data(T data,ResultCodeEnum codeEnum,Integer count){
        Result<T> r = new Result<>();
        r.setCode(codeEnum.getCode());
        r.setSuccess(codeEnum.getStatus());
        r.setMessage(codeEnum.getMsg());
        r.setData(data);
        r.setCount(count);
        return r;
    }
    public Result<T> data(T data){
        this.setData(data);
        return this;
    }


    public Result<T> message(String message){
        this.setMessage(message);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    public Result<T> success(Boolean success){
        this.setSuccess(success);
        return this;
    }
}
