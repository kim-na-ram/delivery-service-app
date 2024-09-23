package com.century21.deliveryserviceapp.store.dto.response;

import com.century21.deliveryserviceapp.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreResponse {
    private Long storeId;
    private String storeName;
    private int minOrderPrice;
    private double averageRating;

    public static StoreResponse from(Store store){
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getMinOrderPrice(),
                store.getAverageRating()
        );
    }
}
