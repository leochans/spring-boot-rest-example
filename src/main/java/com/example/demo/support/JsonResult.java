package com.example.demo.support;

import lombok.Data;

/**
 * 虚拟交易接口返回对象框架，具体的数据在data里，用实现ResultData的对象.
 *
 * @param <T> 模板类型
 * @author yuan.cheng
 */
@Data
class JsonResult<T> {
    /**
     * 0,成功;其他为失败
     */
    private static final int CODE_SUCCESS = 0;

    /**
     * 默认为成功
     */
    private int code = CODE_SUCCESS;
    private String msg = "";
    private T data;

    /**
     * 封装一个正常的返回对象.
     *
     * @param resultData result object
     * @return {@link JsonResult}
     */
    static JsonResult build(Object resultData) {
        JsonResult<Object> result = new JsonResult<>();
        result.setData(resultData);
        return result;
    }

    /**
     * 封装返回一个指定的对象.
     *
     * @param code int value of return code
     * @param msg  String value of message
     * @return {@link JsonResult}
     */
    static JsonResult build(int code, String msg) {
        JsonResult<EmptyObject> result = new JsonResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(new EmptyObject());
        return result;
    }

    /**
     * 封装返回一个成功的对象.
     */
    static JsonResult buildSuccess() {
        return JsonResult.build(CODE_SUCCESS, "successful");
    }
}