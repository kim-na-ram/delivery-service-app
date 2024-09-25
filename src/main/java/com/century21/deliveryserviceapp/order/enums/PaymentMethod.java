package com.century21.deliveryserviceapp.order.enums;

import com.century21.deliveryserviceapp.common.exception.InvalidParameterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.INVALID_PAYMENT_METHOD;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    CARD("카드"),
    CASH("현금"),
    NAVER_PAY("네이버페이"),
    KAKAO_PAY("카카오페이");

    private final String method;

    public static PaymentMethod getPaymentMethod(String method) {
        return Arrays.stream(PaymentMethod.values())
                .filter(paymentMethod -> paymentMethod.getMethod().equals(method))
                .findAny()
                .orElseThrow(() -> new InvalidParameterException(INVALID_PAYMENT_METHOD));
    }
}
