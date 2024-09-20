package com.century21.deliveryserviceapp.menu.dto.request;

import com.century21.deliveryserviceapp.common.enums.Authority;
import lombok.Getter;

@Getter
public class MenuRequest {

    private String menuName;
    private Long price;
}
