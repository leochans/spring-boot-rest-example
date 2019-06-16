package com.example.demo.support;

import com.example.demo.exception.DemoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * rest api common support config.
 *
 * @author yuan.cheng
 */
@Slf4j
@ControllerAdvice
public class RestControllerAdvice implements ResponseBodyAdvice<Object> {

    private static final String PROJECT_BASE_PACKAGE = "com.example.demo";
    /**
     * handler the declare exception define in project.
     * <p>
     *     this kind exceptions define for business error, so response 200 with error message
     * </p>
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

    /**
     * handle wrong query parameter.
     *
     * <p>
     *     this exception indicate that client pass a wrong parameter, so response 400 for client error
     * </p>
     *
     * @param ex of type {@link MethodArgumentTypeMismatchException}
     * @return failed {@link JsonResult} with specific code and message
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JsonResult handleWrongArgument(MethodArgumentTypeMismatchException ex) {
        return JsonResult.build(HttpStatus.BAD_REQUEST.value(),
            String.format("wrong value['%s'] for parameter['%s']", ex.getValue(), ex.getName()));
    }

    /**
     * handler the RuntimeException.
     *
     * <p>
     *     finally in the end handle all the undeclared run time exception, print error log and response 500 for server
     *     error then client will know server's status and deal with this situation correctly
     * </p>
     *
     * @param ex target exception {@link RuntimeException}
     * @return {@link JsonResult}
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JsonResult handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return JsonResult.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType)
            && returnType.getDeclaringClass().getName().startsWith(PROJECT_BASE_PACKAGE)
            && !this.getClass().isAssignableFrom(returnType.getDeclaringClass());
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object resultData, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> clazz, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (null == resultData) {
            return JsonResult.buildSuccess();
        }
        if (resultData instanceof MappingJacksonValue) {
            MappingJacksonValue result = (MappingJacksonValue) resultData;
            Object body = result.getValue();
            result.setValue(JsonResult.build(body));
            return result;
        }

        return JsonResult.build(resultData);
    }
}