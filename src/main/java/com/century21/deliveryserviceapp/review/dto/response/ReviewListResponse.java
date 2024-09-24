package com.century21.deliveryserviceapp.review.dto.response;

import com.century21.deliveryserviceapp.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewListResponse {
    private String storeName;
    private List<ReviewResponse> reviewList;

    @Getter
    public static class ReviewResponse {
        private long reviewId;
        private String manuName;
        private String nickname;
        private int rating;
        private String contents;
        private LocalDateTime createdAt;

        private ReviewResponse(long reviewId, String manuName, String nickname, int rating, String contents, LocalDateTime createdAt) {
            this.reviewId = reviewId;
            this.manuName = manuName;
            this.nickname = nickname;
            this.rating = rating;
            this.contents = contents;
            this.createdAt = createdAt;
        }

        public static ReviewResponse from(Review review) {
            return new ReviewResponse(
                    review.getId(),
                    review.getOrder().getMenuName(),
                    review.getUser().getNickname(),
                    review.getRating(),
                    review.getContents(),
                    review.getCreatedAt()
            );
        }
    }

    private ReviewListResponse(String storeName, List<ReviewResponse> reviewList) {
        this.storeName = storeName;
        this.reviewList = reviewList;
    }

    public static ReviewListResponse from(String storeName, List<Review> reviewList) {
        return new ReviewListResponse(
                storeName,
                reviewList.stream().map(ReviewResponse::from).toList()
        );
    }
}
