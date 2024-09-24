package com.century21.deliveryserviceapp.data;

import com.century21.deliveryserviceapp.entity.Menu;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;

public class MenuMockDataUtil {
    public static MenuRequest menuRequest(int price) {
        return new MenuRequest("menuName", price);
    }

    public static Menu menu(int price) {
        Store store = StoreMockDataUtil.store();
        return Menu.from(menuRequest(price), store);
    }
}
