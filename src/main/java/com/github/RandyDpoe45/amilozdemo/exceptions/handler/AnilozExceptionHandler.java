/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.RandyDpoe45.amilozdemo.exceptions.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.RandyDpoe45.amilozdemo.exceptions.dtos.ExceptionResponse;
import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.AmilozServiceException;
import com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes.ExceptionCodesEnum;
import com.nimbusds.jose.proc.BadJWSException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author randy
 */
@RestControllerAdvice
public class AnilozExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ExceptionResponse handlerGeneralException(AmilozServiceException exception) {
        return ExceptionResponse.builder()
                .exceptionCode(exception.getExceptionCode())
                .response(exception.getMessage())
                .build();
    }

    @ExceptionHandler(BadJWSException.class)
    @ResponseBody
    public ExceptionResponse handlerAuthException(BadJWSException exception) {
        return new ExceptionResponse(exception.getMessage(), ExceptionCodesEnum.INVALID_JWT_TOKEN.getCode());
    }

    @ExceptionHandler
    @ResponseBody
    public ExceptionResponse handlerJsonProcessingException(JsonProcessingException exception) {
        return new ExceptionResponse(
                exception.getMessage(),
                ExceptionCodesEnum.UNPARSEABLE_JSON.getCode()
        );
    }

    @ExceptionHandler
    @ResponseBody
    public ExceptionResponse HandlerException(Exception exception) {
        return new ExceptionResponse(exception.getMessage(), ExceptionCodesEnum.UNEXPECTED_ERROR.getCode());
    }

}
