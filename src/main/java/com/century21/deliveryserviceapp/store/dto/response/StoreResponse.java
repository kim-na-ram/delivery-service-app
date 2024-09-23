package com.century21.deliveryserviceapp.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreResponse {
    private Long storeId;
    private String storeName;
    private int minOrderPrice;
    private double averageRating;
}
