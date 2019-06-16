package com.example.demo.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * json result for generic object.
 *
 * <p>
 *     unified json format response with the form of
 *     <pre>
 *         {"code" : 0,
 *          "msg" : "success",
 *          "data" : {
 *              ...
 *          }
 *         }
 *     </pre>
 *     for object result and
 *     <pre>
 *         {"code" : 0,
 *          "msg" : "success",
 *          "data" : {
 *           "items" : [
 *              ...
 *              ]
 *          }
 *         }
 *     </pre>
 *     for list result and
 *     <pre>
 *         {"code" : 70000,
 *          "msg" : "Some thing wrong in server!",
 *          "data" : {}
 *         }
 *     </pre>
 *     for error
 * </p>
 *
 * @author yuan.cheng
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class JsonResult {
    /**
     * 0 for success other for failed
     */
    private static final int CODE_SUCCESS = 0;
    private static final String MSG_SUCCESS = "success";

    private final int code;
    private final String msg;
    private final Object data;

    /**
     * wrapper a object for success result.
     *
     * @param resultData result object
     * @return {@link JsonResult}
     */
    static JsonResult build(Object resultData) {
        if (resultData instanceof Collection) {
            return new JsonResult(CODE_SUCCESS, MSG_SUCCESS, new ListResult((Collection) resultData));
        }
        return new JsonResult(CODE_SUCCESS, MSG_SUCCESS, resultData);
    }

    /**
     * specify code and msg for result.
     *
     * @param code int value of return code
     * @param msg  String value of message
     * @return {@link JsonResult}
     */
    static JsonResult build(int code, String msg) {
        return new JsonResult(code, msg, new EmptyObject());
    }

    /**
     * return an empty success result.
     * @return {@link JsonResult}
     */
    static JsonResult buildSuccess() {
        return new JsonResult(CODE_SUCCESS, MSG_SUCCESS, new EmptyObject());
    }

    /**
     * EmptyObject.
     *
     * @author yuan.cheng
     */
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static final class EmptyObject {
    }

    /**
     * collection based type wrapper result.
     *
     * @author yuan.cheng
     */
    @Data
    private static final class ListResult {
        private final Collection items;
    }
}