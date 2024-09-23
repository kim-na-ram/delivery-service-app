package com.century21.deliveryserviceapp.order.dto.response;

import com.century21.deliveryserviceapp.entity.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponse {
    private long orderId;
    private String storeName;
    private String menuName;
    private String nickname;
    private String orderStatus;
    private String paymentMethod;
    private int paymentAmount;
    private LocalDateTime orderDate;

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStore().getName(),
                order.getMenuName(),
                order.getUser().getNickname(),
                order.getStatus().getStatus(),
                order.getPaymentMethod().getMethod(),
                order.getMenuPrice(),
                order.getCreatedAt()
        );
    }
}
