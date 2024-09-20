package com.century21.deliveryserviceapp.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    // 공통
    SUCCESS("정상 처리되었습니다."),


    // 사용자
    NOT_FOUND_USER("해당 사용자는 존재하지 않습니다."),
    INVALID_USER_AUTHORITY("해당 사용자 권한은 유효하지 않습니다."),


    // 가게
    NOT_FOUND_STORE("해당 가게는 존재하지 않습니다."),

    // 메뉴


    // 주문


    // 리뷰


    ;

    private final String message;
}
