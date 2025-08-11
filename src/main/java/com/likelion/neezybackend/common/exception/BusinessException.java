package com.likelion.neezybackend.common.exception;

import com.likelion.neezybackend.common.error.ErrorCode;
import lombok.Getter;

@Getter // getter 메소드 자동 생성 lombok 어노테이션
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;   // 에러 코드 ex) NOT_FOUND
    private final String customMessage;  // 사용자 정의 예외 메시지(커스텀)

    // 생성자
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);    // RuntimeException의 생성자에 메시지 전달
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }
}