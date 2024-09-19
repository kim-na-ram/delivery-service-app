package com.century21.deliveryserviceapp.store.dto.request;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class RegisterStoreRequest {
    private String storeName;
    private String introduction;
    private LocalTime openingTime;
    private LocalTime closedTime;
    private int minOrderPrice;
}