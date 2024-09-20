package com.century21.deliveryserviceapp.store.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class UpdateStoreRequest {
    private String storeName;
    private String introduction;
    private LocalTime openingTime;
    private LocalTime closedTime;
    private Integer minOrderPrice;

}