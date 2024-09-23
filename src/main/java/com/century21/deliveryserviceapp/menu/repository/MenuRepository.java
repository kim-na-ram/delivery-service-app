package com.century21.deliveryserviceapp.menu.repository;

import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.NOT_FOUND_MENU;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndDeletedAtIsNull(Long id);

    Optional<Menu> findByIdAndStoreIdAndDeletedAtIsNull(long id, long storeId);

    default Menu findByMenuId(long menuId, long storeId) {
        return findByIdAndStoreIdAndDeletedAtIsNull(menuId, storeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MENU));
    }
}
