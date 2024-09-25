package com.century21.deliveryserviceapp.menu.controller;

import com.century21.deliveryserviceapp.common.annotaion.Auth;
import com.century21.deliveryserviceapp.common.response.SuccessResponse;
import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;
import com.century21.deliveryserviceapp.menu.dto.response.MenuResponse;
import com.century21.deliveryserviceapp.menu.service.MenuService;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 생성
    @PostMapping("/api/stores/{storeId}/menus")
    public ResponseEntity<SuccessResponse<MenuResponse>> saveMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody MenuRequest menuRequest) {
        MenuResponse menuResponse = menuService.saveMenu(authUser, storeId, menuRequest);
        return ResponseEntity.ok(SuccessResponse.of(menuResponse));
    }

    // 메뉴 수정
    @PutMapping("/api/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<SuccessResponse<MenuResponse>> updateMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @Valid @RequestBody MenuRequest menuRequest) {
        MenuResponse menuResponse = menuService.updateMenu(authUser, storeId, menuId, menuRequest);
        return ResponseEntity.ok(SuccessResponse.of(menuResponse));
    }

    // 메뉴 삭제
    @DeleteMapping("/api/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<SuccessResponse<Void>> deleteMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId) {
        menuService.deleteMenu(authUser, storeId, menuId);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }
}
