package com.century21.deliveryserviceapp.store.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreDetailResponse {
    private String storeName;
    private String introduction;
    private LocalTime openingTime;
    private LocalTime closedTime;
    private int minOrderPrice;
}
