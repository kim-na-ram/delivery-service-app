package com.century21.deliveryserviceapp.order.dto.response;

import com.century21.deliveryserviceapp.entity.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderListResponse {
    private long orderId;
    private String storeName;
    private String orderStatus;
    private String menuName;
    private int paymentAmount;
    private LocalDateTime orderDate;

    public static List<OrderListResponse> from(List<Order> orderList) {
        return orderList.stream()
                .map(order ->
                        new OrderListResponse(order.getId(),
                                order.getStore().getName(),
                                order.getStatus().getStatus(),
                                order.getMenuName(),
                                order.getMenuPrice(),
                                order.getCreatedAt())
                )
                .toList();
    }
}
