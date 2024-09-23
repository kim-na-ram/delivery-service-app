package com.century21.deliveryserviceapp.store.repository;

import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.NOT_FOUND_STORE;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT COUNT(*) FROM Store s WHERE s.user.id=:userId AND s.deletedAt IS NULL")
    int findStoreCount(@Param("userId") Long userId);

    Page<Store> findByNameContainingAndDeletedAtIsNull(String name, Pageable pageable);

    Page<Store> findByDeletedAtIsNull(Pageable pageable);

    Optional<Store> findByIdAndDeletedAtIsNull(Long storeId);

    Optional<Store> findByIdAndDeletedAtIsNull(long id);

    default Store findByStoreId(long storeId) {
        return findByIdAndDeletedAtIsNull(storeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
    }
}
