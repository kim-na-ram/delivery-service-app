package com.century21.deliveryserviceapp.order.controller;

import com.century21.deliveryserviceapp.annotation.OrderLogger;
import com.century21.deliveryserviceapp.common.annotaion.Auth;
import com.century21.deliveryserviceapp.common.response.SuccessResponse;
import com.century21.deliveryserviceapp.order.dto.request.ChangeOrderStatusRequest;
import com.century21.deliveryserviceapp.order.dto.request.OrderRequest;
import com.century21.deliveryserviceapp.order.dto.response.OrderListResponse;
import com.century21.deliveryserviceapp.order.dto.response.OrderResponse;
import com.century21.deliveryserviceapp.order.service.OrderService;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    @OrderLogger
    @PostMapping("/orders")
    public ResponseEntity<SuccessResponse<OrderResponse>> newOrder(
            @Auth AuthUser authUser,
            @Valid @RequestBody OrderRequest orderRequest
    ) {
        OrderResponse orderResponse = orderService.saveOrder(authUser, orderRequest);
        return ResponseEntity.ok(SuccessResponse.of(orderResponse));
    }

    @GetMapping("/orders")
    public ResponseEntity<SuccessResponse<List<OrderListResponse>>> getOrderList(@Auth AuthUser authUser) {
        List<OrderListResponse> orderListResponse = orderService.getOrderList(authUser);
        return ResponseEntity.ok(SuccessResponse.of(orderListResponse));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponse>> getOrderDetail(
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ) {
        OrderResponse orderResponse = orderService.getOrder(authUser, orderId);
        return ResponseEntity.ok(SuccessResponse.of(orderResponse));
    }

    @OrderLogger
    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponse>> changeOrderStatus(
            @Auth AuthUser authUser,
            @PathVariable Long orderId,
            @Valid @RequestBody ChangeOrderStatusRequest changeOrderStatusRequest
    ) {
        OrderResponse orderResponse = orderService.changeOrderStatus(authUser, orderId, changeOrderStatusRequest);
        return ResponseEntity.ok(SuccessResponse.of(orderResponse));
    }

    @OrderLogger
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponse>> cancelOrder(
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ) {
        OrderResponse orderResponse = orderService.deleteOrder(authUser, orderId);
        return ResponseEntity.ok(SuccessResponse.of(orderResponse));
    }
}
