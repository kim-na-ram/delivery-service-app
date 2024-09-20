package com.century21.deliveryserviceapp.menu.controller;

import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.menu.common.annotation.Auth;
import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;
import com.century21.deliveryserviceapp.menu.dto.response.MenuResponse;
import com.century21.deliveryserviceapp.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 생성 ( 토큰 필요 )
    // 사장님은 본인 가게에만 메뉴를 등록할 수 있습니다.
    @PostMapping("/api/stores/{storeId}/menus")
    public MenuResponse saveMenu(@PathVariable Long storeId, @RequestBody MenuRequest menuRequest){
        return menuService.saveMenu(storeId, menuRequest);
    }

    // 메뉴 조회
    // 메뉴는 단독으로 조회할 수 없으며, 가게 조회 시 함께 조회됩니다.
    // 가게 메뉴 조회 시, 삭제된 메뉴는 나타나지 않습니다. 주문 내역 조회 시 에는 삭제된 메뉴의 정보도 나타납니다.


    // 메뉴 수정 ( 토큰 필요 )
    // 사장님은 본인 가게에만 메뉴를 수정할 수 있습니다.

    // 메뉴 삭제 ( 토큰 필요 )
    // 사장님은 본인 가게의 메뉴만 삭제할 수 있습니다.
    // 삭제 시, 메뉴의 상태만 삭제 상태로 변경됩니다.

}
