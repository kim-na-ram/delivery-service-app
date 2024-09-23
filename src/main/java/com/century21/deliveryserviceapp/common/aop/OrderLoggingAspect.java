package com.century21.deliveryserviceapp.common.aop;

import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.common.response.SuccessResponse;
import com.century21.deliveryserviceapp.order.dto.response.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.NOT_FOUND_ORDER;

@Slf4j
@Aspect
@Component
public class OrderLoggingAspect {
    @Pointcut("@annotation(com.century21.deliveryserviceapp.annotation.OrderLogger)")
    private void orderLoggerPointcut() {}

    @AfterReturning(pointcut = "orderLoggerPointcut()", returning = "result")
    public void beforeOrderTracker(Object result) {
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;

        SuccessResponse<?> response = (SuccessResponse<?>) responseEntity.getBody();
        if (response == null) {
            throw new NotFoundException(NOT_FOUND_ORDER);
        }

        OrderResponse orderResponse = (OrderResponse) response.getData();

        LocalDateTime requestTime = LocalDateTime.now();
        long orderId = orderResponse.getOrderId();
        String storeName = orderResponse.getStoreName();

        log.info("[Request] time : {}", requestTime);
        log.info("[Request] orderId : {}", orderId);
        log.info("[Request] storeName : {}", storeName);
    }
}
