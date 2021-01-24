/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller.handlerex;

import com.wanl.entity.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@ControllerAdvice
@ResponseBody
public class ImunityExceptionHandler {
    private static final String LOGEXCEPTIONFORMAT = "Capture Exception By ImunityHandlerException: Code: %s Detail: %s";
    private static Logger logger = LogManager.getLogger(ImunityExceptionHandler.class.getName());

    @ExceptionHandler({ RuntimeException.class })
    public String noHandlerFound(RuntimeException ex) {
        return resultFormat(Integer.valueOf(-1001), ex);
    }

    @ExceptionHandler({ NullPointerException.class })
    public String nullPointerExceptionHandler(NullPointerException ex) {
        return resultFormat(Integer.valueOf(-2001), ex);
    }

    @ExceptionHandler({ ClassCastException.class })
    public String classCastExceptionHandler(ClassCastException ex) {
        return resultFormat(Integer.valueOf(-3001), ex);
    }

    @ExceptionHandler({ IOException.class })
    public String iOExceptionHandler(IOException ex) {
        return resultFormat(Integer.valueOf(-4001), ex);
    }

    @ExceptionHandler({ NoSuchMethodException.class })
    public String noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return resultFormat(Integer.valueOf(-5001), ex);
    }

    @ExceptionHandler({ IndexOutOfBoundsException.class })
    public String indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return resultFormat(Integer.valueOf(-6001), ex);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public String requestNotReadable(HttpMessageNotReadableException ex) {
        return resultFormat(Integer.valueOf(-7001), ex);
    }

    @ExceptionHandler({ TypeMismatchException.class })
    public String requestTypeMismatch(TypeMismatchException ex) {
        return resultFormat(Integer.valueOf(-8001), ex);
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class })
    public String requestMissingServletRequest(MissingServletRequestParameterException ex) {
        return resultFormat(Integer.valueOf(-9001), ex);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public String request405(HttpRequestMethodNotSupportedException ex) {
        return resultFormat(Integer.valueOf(-10002), ex);
    }

    @ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
    public String request406(HttpMediaTypeNotAcceptableException ex) {
        return resultFormat(Integer.valueOf(-1100), ex);
    }

    @ExceptionHandler({ org.springframework.beans.ConversionNotSupportedException.class,
            org.springframework.http.converter.HttpMessageNotWritableException.class })
    public String server500(RuntimeException ex) {
        return resultFormat(Integer.valueOf(-12001), ex);
    }

    @ExceptionHandler({ StackOverflowError.class })
    public String requestStackOverflow(StackOverflowError ex) {
        return resultFormat(Integer.valueOf(-13001), ex);
    }

    @ExceptionHandler({ Exception.class })
    public String exception(Exception ex) {
        return resultFormat(Integer.valueOf(-14001), ex);
    }

    private <T extends Throwable> String resultFormat(Integer code, T ex) {
        ex.printStackTrace();
        logger.error(String.format(LOGEXCEPTIONFORMAT,
                new Object[] { code, ex.getMessage() }));
        return Result.failed(code, ex.getMessage());
    }
}
