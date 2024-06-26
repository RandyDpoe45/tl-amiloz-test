package com.github.RandyDpoe45.amilozdemo.exceptions.exceptiontypes;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AmilozServiceException extends RuntimeException {

    private String exceptionCode;

    public AmilozServiceException(ExceptionCodesEnum exceptionCode) {
        super(exceptionCode.getDefaultMessage());
        this.exceptionCode = exceptionCode.getCode();
    }

    public AmilozServiceException(ExceptionCodesEnum exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode.getCode();
    }

    public AmilozServiceException(Throwable cause, ExceptionCodesEnum exceptionCode, String message) {
        super(message, cause);
        this.exceptionCode = exceptionCode.getCode();
    }
}
