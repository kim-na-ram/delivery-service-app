package com.century21.deliveryserviceapp.menu.dto.response;

import com.century21.deliveryserviceapp.entity.Menu;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;
import lombok.Getter;

@Getter
public class MenuResponse {

    private final long menuId;
    private final String name;
    private final int price;


    private MenuResponse(long menuId, String name, int price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static MenuResponse from(long menuId, String name, int price) {
        return new MenuResponse(menuId, name, price);
    }
}
