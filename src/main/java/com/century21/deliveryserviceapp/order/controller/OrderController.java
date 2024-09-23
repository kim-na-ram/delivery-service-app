package com.century21.deliveryserviceapp.order.controller;

import com.century21.deliveryserviceapp.annotation.OrderLogger;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.common.response.SuccessResponse;
import com.century21.deliveryserviceapp.order.dto.request.ChangeOrderStatusRequest;
import com.century21.deliveryserviceapp.order.dto.request.OrderRequest;
import com.century21.deliveryserviceapp.order.dto.response.OrderListResponse;
import com.century21.deliveryserviceapp.order.dto.response.OrderResponse;
import com.century21.deliveryserviceapp.order.service.OrderService;
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
//            AuthUser authUser,
            @Valid @RequestBody OrderRequest orderRequest
    ) {
        // TODO [ORDER] 삭제 예정 : 필터 적용 후 아래 코드 삭제
//        AuthUser authUser = new AuthUser(1L, Authority.OWNER);
        AuthUser authUser = new AuthUser(2L, Authority.USER);
        OrderResponse orderResponse = orderService.saveOrder(authUser, orderRequest);
        return ResponseEntity.ok(SuccessResponse.of(orderResponse));
    }

    @GetMapping("/orders")
    public ResponseEntity<SuccessResponse<List<OrderListResponse>>> getOrderList() {
        // TODO [ORDER] 삭제 예정 : 필터 적용 후 아래 코드 삭제
        AuthUser authUser = new AuthUser(1L, Authority.USER);
        List<OrderListResponse> orderListResponse = orderService.getOrderList(authUser);
        return ResponseEntity.ok(SuccessResponse.of(orderListResponse));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponse>> getOrderDetail(
//            AuthUser authUser,
            @PathVariable Long orderId
    ) {
        // TODO [ORDER] 삭제 예정 : 필터 적용 후 아래 코드 삭제
        AuthUser authUser = new AuthUser(2L, Authority.USER);
        OrderResponse orderResponse = orderService.getOrder(authUser, orderId);
        return ResponseEntity.ok(SuccessResponse.of(orderResponse));
    }

    @OrderLogger
    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponse>> changeOrderStatus(
//            AuthUser authUser,
            @PathVariable Long orderId,
            @Valid @RequestBody ChangeOrderStatusRequest changeOrderStatusRequest
    ) {
        // TODO [ORDER] 삭제 예정 : 필터 적용 후 아래 코드 삭제
        AuthUser authUser = new AuthUser(1L, Authority.OWNER);
        OrderResponse orderResponse = orderService.changeOrderStatus(authUser, orderId, changeOrderStatusRequest);
        return ResponseEntity.ok(SuccessResponse.of(orderResponse));
    }

    @OrderLogger
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponse>> cancelOrder(
//            AuthUser authUser,
            @PathVariable Long orderId
    ) {
        // TODO [ORDER] 삭제 예정 : 필터 적용 후 아래 코드 삭제
        AuthUser authUser = new AuthUser(4L, Authority.USER);
        OrderResponse orderResponse = orderService.deleteOrder(authUser, orderId);
        return ResponseEntity.ok(SuccessResponse.of(orderResponse));
    }
}
