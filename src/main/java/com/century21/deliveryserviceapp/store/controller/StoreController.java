package com.century21.deliveryserviceapp.store.controller;

import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.response.RegisterStoreResponse;
import com.century21.deliveryserviceapp.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    //가게 등록
    @PostMapping("/{userId}")
    public RegisterStoreResponse registerStore(@PathVariable("userId") Long userId,@RequestBody RegisterStoreRequest registerStoreRequest){
        return storeService.registerStore(userId,registerStoreRequest);
    }

    //가게 단건 조회

}
