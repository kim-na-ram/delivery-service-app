package com.century21.deliveryserviceapp.store.dto.response;

import com.century21.deliveryserviceapp.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStoreResponse {
    private Long storeId;
    private String storeName;
    private String introduction;
    private LocalTime openingTime;
    private LocalTime closedTime;
    private int minOrderPrice;


    public static RegisterStoreResponse from(Store savedStore){
        return new RegisterStoreResponse(
                savedStore.getId(),
                savedStore.getName(),
                savedStore.getIntroduction(),
                savedStore.getOpeningTime(),
                savedStore.getClosedTime(),
                savedStore.getMinOrderPrice()
        );
    }
}
