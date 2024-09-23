package com.century21.deliveryserviceapp.menu.repository;

import com.century21.deliveryserviceapp.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndDeletedAtIsNull(Long id);
}
