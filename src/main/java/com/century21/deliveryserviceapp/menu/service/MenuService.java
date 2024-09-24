package com.century21.deliveryserviceapp.menu.service;


import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.common.exception.ResponseCode;
import com.century21.deliveryserviceapp.common.exception.UnauthorizedException;
import com.century21.deliveryserviceapp.entity.Menu;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;
import com.century21.deliveryserviceapp.menu.dto.response.MenuResponse;
import com.century21.deliveryserviceapp.menu.repository.MenuRepository;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;


    @Transactional
    public MenuResponse saveMenu(AuthUser authUser, Long storeId, MenuRequest menuRequest) {
        // 사장님 권한 확인
        if (!authUser.getAuthority().equals(Authority.OWNER.name())) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // Store 정보 조회
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_STORE));

        // 사장님의 본인 가게인지 확인
        if (!store.getUser().getId().equals(authUser.getUserId())) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // 메뉴 저장
        Menu newMenu = Menu.from(menuRequest, store);
        Menu savedMenu = menuRepository.save(newMenu);
        return MenuResponse.from(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice());
    }

    @Transactional
    public MenuResponse updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuRequest menuRequest) {
        // 사장님 권한 확인
        if (!authUser.getAuthority().equals(Authority.OWNER.name())) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // Store 정보 조회
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_STORE));

        // Menu 정보 조회
        Menu menu = menuRepository.findByIdAndDeletedAtIsNull(menuId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MENU));

        // 사장님의 본인 가게인지 확인
        if (!store.getUser().getId().equals(authUser.getUserId())) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // 메뉴 수정
        menu.updateMenu(menuRequest.getName(), menuRequest.getPrice());

        return MenuResponse.from(menu.getId(), menu.getName(), menu.getPrice());
    }

    @Transactional
    public void deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        // 사장님 권한 확인
        if (!authUser.getAuthority().equals(Authority.OWNER.name())) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // Store 정보 조회
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_STORE));

        // Menu 정보 조회
        Menu menu = menuRepository.findByIdAndDeletedAtIsNull(menuId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MENU));

        // 사장님의 본인 가게인지 확인
        if (!store.getUser().getId().equals(authUser.getUserId())) {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_AUTHORITY);
        }

        // 메뉴 삭제
        menu.deleteMenu();
        menuRepository.save(menu);
    }
}
