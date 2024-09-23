package com.century21.deliveryserviceapp.store.dto.response;

import com.century21.deliveryserviceapp.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class StoreDetailResponse {
    private String storeName;
    private String introduction;
    private LocalTime openingTime;
    private LocalTime closedTime;
    private int minOrderPrice;
    private double averageRating;
    private List<MenuReponse> menuList;

    public static StoreDetailResponse from(Store store,List<MenuReponse> menuList){
        return new StoreDetailResponse(
                store.getName(),
                store.getIntroduction(),
                store.getOpeningTime(),
                store.getClosedTime(),
                store.getMinOrderPrice(),
                store.getAverageRating(),
                menuList
        );
    }
}
