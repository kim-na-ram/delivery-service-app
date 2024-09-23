package com.century21.deliveryserviceapp.store.dto.response;

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
    private List<MenuDto> menuList;
}
