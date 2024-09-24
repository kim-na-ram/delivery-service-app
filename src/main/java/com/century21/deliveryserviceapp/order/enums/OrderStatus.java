package com.century21.deliveryserviceapp.order.enums;

import com.century21.deliveryserviceapp.common.exception.InvalidParameterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.INVALID_ORDER_STATUS;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING_ACCEPTANCE("수락 대기", 1),
    CANCELED("주문 취소", 2),
    COOKING("조리중", 3),
    DELIVERY_STARTED("배달 시작", 4),
    DELIVERY_COMPLETED("배달 완료", 5);

    private final String status;
    private final int step;

    public static OrderStatus getStatus(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.getStatus().equals(status))
                .findAny()
                .orElseThrow(() -> new InvalidParameterException(INVALID_ORDER_STATUS));
    }
}
