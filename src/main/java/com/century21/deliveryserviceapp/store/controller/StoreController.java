package com.century21.deliveryserviceapp.store.controller;

import com.century21.deliveryserviceapp.common.response.SuccessResponse;
import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.response.StoreDetailResponse;
import com.century21.deliveryserviceapp.store.dto.response.RegisterStoreResponse;
import com.century21.deliveryserviceapp.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    //가게 등록
    @PostMapping("/{userId}")
    public ResponseEntity<SuccessResponse<RegisterStoreResponse>> registerStore(@PathVariable("userId") Long userId, @RequestBody RegisterStoreRequest registerStoreRequest){
        return ResponseEntity.ok(SuccessResponse.of(storeService.registerStore(userId,registerStoreRequest)));
    }

    //가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<StoreDetailResponse>> getStore(@PathVariable("storeId") Long storeId){
        return ResponseEntity.ok(SuccessResponse.of(storeService.getStore(storeId)));
    }
}
