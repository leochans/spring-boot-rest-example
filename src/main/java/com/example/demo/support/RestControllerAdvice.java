package com.example.demo.support;

import com.example.demo.exception.DemoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;
import java.util.List;


/**
 * 对所有异常的拦截及统一处理.
 *
 * @author yuan.cheng
 */
@Slf4j
@ControllerAdvice
public class RestControllerAdvice implements ResponseBodyAdvice<Object> {

    private static final int RUN_TIME_ERROR_CODE = 70_000;
    private static final String RUN_TIME_ERROR_MESSAGE = "Some thing totally wrong!";

    /**
     * handler the StockHqException.
     *
     * @param ex target exception {@link DemoException}
     * @return {@link JsonResult}
     */
    @ExceptionHandler(DemoException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResult handleStockHqException(DemoException ex) {
        return JsonResult.build(ex.getErrorCode(), ex.getErrorMsg());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonResult handleWrongArgument(MethodArgumentTypeMismatchException ex) {
        return JsonResult.build(400, String.format("wrong parameter [%s]", ex.getName()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonResult handleWrongBody(HttpMessageNotReadableException ex) {
        return JsonResult.build(400, ex.getMessage());
    }

    /**
     * handler the RuntimeException.
     *
     * @param ex target exception {@link RuntimeException}
     * @return {@link JsonResult}
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResult handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return JsonResult.build(RUN_TIME_ERROR_CODE, RUN_TIME_ERROR_MESSAGE);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object beforeBodyWrite(Object resultData, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> clazz, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (resultData instanceof MappingJacksonValue) {
            MappingJacksonValue result = (MappingJacksonValue) resultData;
            Object body = result.getValue();
            result.setValue(JsonResult.build(body));
            return result;
        }

        if (resultData instanceof Collection) {
            return JsonResult.build(new ListResult<Object>((List) resultData));
        }

        return JsonResult.build(resultData);
    }
}