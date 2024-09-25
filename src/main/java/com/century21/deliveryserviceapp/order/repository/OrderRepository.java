package com.century21.deliveryserviceapp.order.repository;

import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.NOT_FOUND_ORDER;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT o FROM Order o JOIN Store s ON o.store.id = s.id WHERE o.id = ?1 AND s.user.id = ?2")
    Optional<Order> findByIdAndOwnerId(long id, long ownerId);

    @Query("SELECT o FROM Order o JOIN Store s ON o.store.id = s.id " +
            "WHERE o.id = ?1 AND s.user.id = ?2 AND o.deletedAt IS NULL")
    Optional<Order> findActiveOrderByIdAndOwnerId(long id, long ownerId);

    @Query("SELECT o FROM Order o JOIN Store s ON o.store.id = s.id WHERE s.user.id = ?1 ORDER BY o.createdAt DESC")
    List<Order> findAllByOwnerId(long ownerId);

    Optional<Order> findByIdAndUserId(Long id, long userId);

    @Query("SELECT o FROM Order o " +
            "WHERE o.id = ?1 AND o.user.id = ?2 AND o.deletedAt IS NULL")
    Optional<Order> findActiveOrderByIdAndUserId(Long id, long userId);

    @Query("FROM Order o JOIN Store s ON o.store.id = s.id WHERE o.user.id = ?1 ORDER BY o.createdAt DESC")
    List<Order> findAllByUserId(long userId);

    default Order findByOrderIdAndUserId(long orderId, long userId) {
        return findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
    }

    default Order findByOrderId(long orderId) {
        return findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
    }
}
