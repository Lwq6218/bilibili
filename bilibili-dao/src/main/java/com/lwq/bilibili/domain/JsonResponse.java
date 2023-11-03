package com.lwq.bilibili.domain;

public class JsonResponse<T> {
    private String msg;
    private String code;
    private T data;

    public JsonResponse(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public JsonResponse(T data) {
        this.data = data;
        this.msg = "成功";
        code = "0";
    }

    public static JsonResponse<String> success() {
        return new JsonResponse<String>((String) null);
    }

    public static JsonResponse<String> success(String code) {
        return new JsonResponse<String>(code);
    }

    public static JsonResponse<String> fail() {
        return new JsonResponse<String>("1", "失败");
    }

    public static JsonResponse<String> fail(String code, String msg) {
        return new JsonResponse<String>(code, msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
