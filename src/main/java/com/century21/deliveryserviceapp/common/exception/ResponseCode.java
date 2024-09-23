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
    DUPLICATE_EMAIL("이미 존재하는 이메일입니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),



    // JWT 관련 예외
    INVALID_JWT_TOKEN("유효하지 않은 JWT 토큰입니다."),
    EXPIRED_JWT_TOKEN("JWT 토큰이 만료되었습니다.");


    // 가게
    NOT_FOUND_STORE("해당 가게는 존재하지 않습니다."),
    MAX_STORE_LIMIT("가게를 3개 이상 등록할 수 없습니다")
    // 메뉴
    NOT_FOUND_MENU("해당 메뉴는 존재하지 않습니다."),

    // 주문


    // 리뷰


    ;

    private final String message;
}
