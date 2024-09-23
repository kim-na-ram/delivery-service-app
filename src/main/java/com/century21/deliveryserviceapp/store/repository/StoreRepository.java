package com.century21.deliveryserviceapp.store.repository;

import com.century21.deliveryserviceapp.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Page<Store> findByNameContaining(String name, Pageable pageable);
}
