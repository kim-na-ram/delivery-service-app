package com.century21.deliveryserviceapp.store.dto.response;

import com.century21.deliveryserviceapp.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponse {
    private String menuName;
    private int price;

    public static MenuResponse from(Menu menu){
        return new MenuResponse(menu.getMenuName(),menu.getPrice());
    }

}
