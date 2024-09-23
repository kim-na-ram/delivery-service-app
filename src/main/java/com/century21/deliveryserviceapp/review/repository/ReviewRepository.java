package com.century21.deliveryserviceapp.review.repository;

import com.century21.deliveryserviceapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.id=:storeId")
    double calculateAverageRating(@Param("storeId") Long storeId);
}
