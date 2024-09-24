package com.century21.deliveryserviceapp.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuRequest {

    @NotBlank(message = "메뉴 이름을 등록해주세요.")
    private String name;

    @NotNull(message = "메뉴 가격을 등록해주세요.")
    private int price;
}
