package com.century21.deliveryserviceapp.review.controller;

import com.century21.deliveryserviceapp.common.annotaion.Auth;
import com.century21.deliveryserviceapp.common.response.SuccessResponse;
import com.century21.deliveryserviceapp.review.dto.request.RegisterReviewRequest;
import com.century21.deliveryserviceapp.review.dto.response.RegisterReviewResponse;
import com.century21.deliveryserviceapp.review.dto.response.ReviewListResponse;
import com.century21.deliveryserviceapp.review.service.ReviewService;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    public final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<SuccessResponse<RegisterReviewResponse>> registerReview(
            @Auth AuthUser authUser,
            @Valid @RequestBody RegisterReviewRequest registerReviewRequest
    ) {
        RegisterReviewResponse registerReviewResponse = reviewService.saveReview(authUser, registerReviewRequest);
        return ResponseEntity.ok(SuccessResponse.of(registerReviewResponse));
    }

    @GetMapping("/reviews/{storeId}")
    public ResponseEntity<SuccessResponse<ReviewListResponse>> getReviewList(
            @PathVariable long storeId,
            @Min(value = 0, message = "별점 최소는 0보다 작을 수 없습니다.")
            @Max(value = 5, message = "별점 최소는 5보다 클 수 없습니다.")
            @RequestParam(defaultValue = "0") Integer ratingMin,
            @Max(value = 5, message = "별점 최대는 5보다 클 수 없습니다.")
            @Min(value = 0, message = "별점 최대는 0보다 작을 수 없습니다.")
            @RequestParam(defaultValue = "5") Integer ratingMax
    ) {
        ReviewListResponse reviewListResponse = reviewService.getReviewList(storeId, ratingMin, ratingMax);
        return ResponseEntity.ok(SuccessResponse.of(reviewListResponse));
    }
}
