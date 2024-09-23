package com.century21.deliveryserviceapp.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStoreRequest {
    @NotBlank
    private String storeName;
    @NotBlank
    private String introduction;
    @NotNull
    private LocalTime openingTime;
    @NotNull
    private LocalTime closedTime;
    @NotNull
    private Integer minOrderPrice;
}