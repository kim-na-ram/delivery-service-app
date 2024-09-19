package com.century21.deliveryserviceapp.store.repository;

import com.century21.deliveryserviceapp.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
