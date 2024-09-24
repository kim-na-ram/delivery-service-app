package com.century21.deliveryserviceapp.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChangeOrderStatusRequest {
    @NotBlank(message = "주문 상태는 필수입니다.")
    private String orderStatus;
}
