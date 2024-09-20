package com.century21.deliveryserviceapp.menu.dto.response;

import lombok.Getter;

@Getter
public class MenuResponse {

    private final Long menuId;
    private final String menuName;
    private final Long price;

    public MenuResponse(Long menuId, String menuName, Long price) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
    }
}
