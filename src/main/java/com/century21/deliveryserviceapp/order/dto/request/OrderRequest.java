package com.century21.deliveryserviceapp.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "가게 고유번호는 필수입니다.")
    private Long storeId;
    @NotNull(message = "메뉴 고유번호는 필수입니다.")
    private Long menuId;
    @NotBlank(message = "결제 방법은 필수입니다.")
    private String paymentMethod;
}
