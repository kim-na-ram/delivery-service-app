package com.century21.deliveryserviceapp.review.repository;

import com.century21.deliveryserviceapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.id=:storeId")
    double calculateAverageRating(@Param("storeId") Long storeId);

    boolean existsByOrderId(Long orderId);

    @Query("SELECT r FROM Review r WHERE r.store.id = :storeId AND r.rating BETWEEN :min AND :max")
    List<Review> findAllByStoreIdAndRatingRange(@Param("storeId") Long storeId, @Param("min") int min, @Param("max") int max);
}
