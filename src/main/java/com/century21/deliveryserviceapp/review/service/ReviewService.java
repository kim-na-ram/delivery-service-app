package com.century21.deliveryserviceapp.review.service;

import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.common.exception.DuplicateException;
import com.century21.deliveryserviceapp.common.exception.InvalidParameterException;
import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.Order;
import com.century21.deliveryserviceapp.entity.Review;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.order.enums.OrderStatus;
import com.century21.deliveryserviceapp.order.repository.OrderRepository;
import com.century21.deliveryserviceapp.review.dto.request.RegisterReviewRequest;
import com.century21.deliveryserviceapp.review.dto.response.RegisterReviewResponse;
import com.century21.deliveryserviceapp.review.dto.response.ReviewListResponse;
import com.century21.deliveryserviceapp.review.repository.ReviewRepository;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import com.century21.deliveryserviceapp.store.service.StoreService;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final StoreService storeService;

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    public RegisterReviewResponse saveReview(AuthUser authUser, RegisterReviewRequest reviewRequest) {
        if(authUser.getAuthority().equals(Authority.OWNER.name())) {
            throw new InvalidParameterException(UNAUTHORIZED_REVIEW);
        }

        // orderId 와 userId 로 존재하는지 확인
        Order order = orderRepository.findByOrderIdAndUserId(reviewRequest.getOrderId(), authUser.getUserId());

        // store 가 존재하는지 확인
        Store store = order.getStore();

        // 가게가 폐업했다면 리뷰 등록 불가능
        if (store.getDeletedAt() != null) {
            throw new NotFoundException(NOT_FOUND_STORE);
        }

        // 배달완료된 상태가 아니라면 리뷰 등록 불가능
        if (order.getStatus() != OrderStatus.DELIVERY_COMPLETED) {
            throw new InvalidParameterException(INVALID_REGISTER_REVIEW_NOT_DELIVERED_ORDER);
        }

        // 이미 작성된 리뷰가 있다면 리뷰 등록 불가능
        boolean isExistReview = reviewRepository.existsByOrderId(reviewRequest.getOrderId());
        if (isExistReview) {
            throw new DuplicateException(DUPLICATED_REVIEW);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reviewableDuration = order.getUpdatedAt().plusDays(3);

        // 배달완료 이후 3일이 지나면 리뷰 등록 불가능
        if (reviewableDuration.isBefore(now)) {
            throw new InvalidParameterException(INVALID_REGISTER_REVIEW_AFTER_3_DAYS);
        }

        Review review = Review.from(reviewRequest, order, order.getUser(), store);
        reviewRepository.save(review);

        // 가게 평균 별점 업데이트
        double averageRating = reviewRepository.calculateAverageRating(order.getStore().getId());
        storeService.updateStoreAverageRating(store, averageRating);

        return RegisterReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public ReviewListResponse getReviewList(long storeId, int min, int max) {
        // 별점 최소가 최대보다 높으면 리뷰 조회 불가능
        if (min > max) {
            throw new InvalidParameterException(INVALID_RATING_RANGE_MAX_UNDER_MIN);
        }

        // 가게가 폐업했는지 확인
        Store store = storeRepository.findByStoreId(storeId);

        List<Review> reviewList = reviewRepository.findAllByStoreIdAndRatingRange(storeId, min, max);
        return ReviewListResponse.from(store.getName(), reviewList);
    }
}
