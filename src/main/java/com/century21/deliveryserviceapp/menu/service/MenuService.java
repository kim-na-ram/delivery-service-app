package com.century21.deliveryserviceapp.menu.service;


import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.common.exception.ResponseCode;
import com.century21.deliveryserviceapp.common.exception.UnauthorizedException;
import com.century21.deliveryserviceapp.entity.Menu;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;
import com.century21.deliveryserviceapp.menu.dto.response.MenuResponse;
import com.century21.deliveryserviceapp.menu.repository.MenuRepository;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    // 메뉴 생성 ( 토큰 필요 )
    // 사장님은 본인 가게에만 메뉴를 등록할 수 있습니다.
    @Transactional
    public MenuResponse saveMenu(Long storeId, MenuRequest menuRequest) {

        // Store 정보 조회
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_STORE));

        // TODO [OWNER Authorization Check]
        if (store.getUser().getId() != 2) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // 메뉴 저장
        Menu newMenu = Menu.from(menuRequest, store);
        Menu savedMenu = menuRepository.save(newMenu);
        return MenuResponse.from(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice());
    }

    @Transactional
    public MenuResponse updateMenu(Long storeId, Long menuId, MenuRequest menuRequest) {

        // Store 정보 조회
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_STORE));

        // Menu 정보 조회
        Menu menu = menuRepository.findByIdAndDeletedIsNull(menuId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MENU));

        // 사장님의 가게인지아닌지
        // 1 = {userIdFromToken}
        // TODO [OWNER Authorization Check]
        if (store.getUser().getId() != 2) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // 메뉴 수정
        menu.updateMenu(menuRequest.getName(), menuRequest.getPrice());

        return MenuResponse.from(menu.getId(), menu.getName(), menu.getPrice());
    }

    @Transactional
    public void deleteMenu(Long storeId, Long menuId) {
        // Store 정보 조회
        // TODO [MENU -> STORE]
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MENU));

        // Menu 정보 조회
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MENU));

        // 사장님의 가게인지아닌지
        // 1 = {userIdFromToken}
        // TODO [OWNER Authorization Check]
        if (store.getUser().getId() != 2) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // 메뉴 삭제
        menu.deleteMenu();
    }
}
