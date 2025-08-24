package com.likelion.neezybackend.global.config.template;

import com.likelion.neezybackend.common.error.ErrorCode;
import com.likelion.neezybackend.common.error.SuccessCode;
import lombok.*;

@Getter // getter 메소드 자동 생성 lombok 어노테이션
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 모든 필드 값을 매개변수로 받는 생성자 자동 생성
@RequiredArgsConstructor(access = AccessLevel.PRIVATE) // final 붙은 필드만 매개변수로 받는 생성자 자동 생성
@Builder // 빌더 패턴
public class ApiResTemplate<T> {

    private final int code;       // 응답 코드 (200, 404 등)
    private final String message; // 응답 메시지
    private T data;               // 응답 데이터 (제네릭 형식으로 다양한 타입 수용 가능)

    // 데이터 없는 성공 응답
    public static ApiResTemplate successWithNoContent(SuccessCode successCode) {
        return new ApiResTemplate<>(successCode.getHttpStatusCode(), successCode.getMessage());
    }

    // 데이터 포함한 성공 응답
    public static <T> ApiResTemplate<T> successResponse(SuccessCode successCode, T data) {
        return new ApiResTemplate<>(successCode.getHttpStatusCode(), successCode.getMessage(), data);
    }

    // 에러 응답 (커스텀 메시지 포함)
    public static ApiResTemplate errorResponse(ErrorCode errorCode, String customMessage) {
        return new ApiResTemplate<>(errorCode.getHttpStatusCode(), customMessage);
    }

}

