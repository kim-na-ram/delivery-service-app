package com.century21.deliveryserviceapp.review.repository;

import com.century21.deliveryserviceapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
