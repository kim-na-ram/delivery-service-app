package com.century21.deliveryserviceapp.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    EXPIRED_JWT_TOKEN("JWT 토큰이 만료되었습니다."),



    // 가게
    NOT_FOUND_STORE("해당 가게는 존재하지 않습니다."),

    MAX_STORE_LIMIT("가게를 3개 이상 등록할 수 없습니다"),



    // 메뉴
    NOT_FOUND_MENU("해당 메뉴는 존재하지 않습니다."),

    // 주문
    NOT_FOUND_ORDER("해당 주문은 존재하지 않습니다."),
    UNAUTHORIZED_ORDER("주문은 사용자만 가능합니다."),
    UNAUTHORIZED_CHANGE_ORDER_STATUS("주문 상태 변경은 사장님만 가능합니다."),
    INVALID_PAYMENT_METHOD("해당 결제 수단은 지원되지 않습니다."),
    INVALID_ORDER_TIME("현재 영업 준비중이기에 주문이 불가능합니다."),
    INVALID_MINIMAL_ORDER_PRICE("최소 주문 금액을 만족해야 합니다."),
    INVALID_ORDER_STATUS("해당 주문 상태는 유효하지 않습니다."),
    INVALID_CHANGE_ORDER_STATUS("이전 주문 상태로 돌아갈 수 없습니다."),
    INVALID_CHANGE_SAME_ORDER_STATUS("현재 주문 상태와 동일합니다."),
    INVALID_CHANGE_ORDER_STATUS_TO_CANCEL("주문 상태 변경으로는 주문 취소를 할 수 없습니다."),
    INVALID_CANCEL_ORDER_FOR_USER("주문 취소는 주문이 수락되기 전에만 가능합니다."),
    INVALID_CANCEL_ORDER_FOR_OWNER("주문 취소는 주문을 수락하기 전에만 가능합니다."),

    // 리뷰


    ;

    private final String message;
}
