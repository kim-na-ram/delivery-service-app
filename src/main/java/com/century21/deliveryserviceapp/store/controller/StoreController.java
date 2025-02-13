package com.century21.deliveryserviceapp.store.controller;

import com.century21.deliveryserviceapp.common.annotaion.Auth;
import com.century21.deliveryserviceapp.common.response.SuccessResponse;
import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.request.UpdateStoreRequest;
import com.century21.deliveryserviceapp.store.dto.response.StoreDetailResponse;
import com.century21.deliveryserviceapp.store.dto.response.RegisterStoreResponse;
import com.century21.deliveryserviceapp.store.dto.response.StoreResponse;
import com.century21.deliveryserviceapp.store.dto.response.UpdateStoreResponse;
import com.century21.deliveryserviceapp.store.service.StoreService;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    //가게 등록
    @PostMapping()
    public ResponseEntity<SuccessResponse<RegisterStoreResponse>> registerStore(@Auth AuthUser authUser, @RequestBody @Valid RegisterStoreRequest registerStoreRequest){
        return ResponseEntity.ok(SuccessResponse.of(storeService.registerStore(authUser.getUserId(),registerStoreRequest)));
    }

    //가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<StoreDetailResponse>> getStore(@PathVariable("storeId") Long storeId){
        return ResponseEntity.ok(SuccessResponse.of(storeService.getStore(storeId)));
    }

    //가게 리스트 조회
    @GetMapping()
    public ResponseEntity<SuccessResponse<Page<StoreResponse>>> getStores(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNumber
    ){
        return ResponseEntity.ok(SuccessResponse.of(storeService.getStores(name,pageSize,pageNumber)));
    }

    //가게 수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<UpdateStoreResponse>> updateStore(@Auth AuthUser authUser,@PathVariable("storeId") Long storeId, @RequestBody UpdateStoreRequest updateStoreRequest){
        return ResponseEntity.ok(SuccessResponse.of(storeService.updateStore(authUser.getUserId(),storeId,updateStoreRequest)));
    }

    //가게 폐업
    @DeleteMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<Void>> deleteStore(@Auth AuthUser authUser, @PathVariable("storeId") Long storeId){
        storeService.deleteStore(authUser.getUserId(),storeId);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }
}
