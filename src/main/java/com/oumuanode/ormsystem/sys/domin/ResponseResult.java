package com.oumuanode.ormsystem.sys.domin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oumuanode.ormsystem.enmus.AppHttpCodeEnum;


import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public ResponseResult() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.message = AppHttpCodeEnum.SUCCESS.getMsg();
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseResult errorResult(int code, String message) {
        ResponseResult result = new ResponseResult();
        return result.error(code, message);
    }
    public static ResponseResult okResult(AppHttpCodeEnum success, String message) {
        ResponseResult result = new ResponseResult();
        return result;
    }

    public static <T> ResponseResult<T> success(String message){
        return new ResponseResult<>(200,message,null);
    }
    public static ResponseResult okResult(int code, String message) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, message);
    }

    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMsg());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> ResponseResult<T> success(T data){
        return new ResponseResult<>(200,"success",data);
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getMsg());
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums, String message){
        return setAppHttpCodeEnum(enums,message);
    }



    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String message){
        return okResult(enums.getCode(),message);
    }



    public ResponseResult<?> error(Integer code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        return this;
    }

    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}