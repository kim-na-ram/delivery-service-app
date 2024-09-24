package com.century21.deliveryserviceapp.review.dto.response;

import com.century21.deliveryserviceapp.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegisterReviewResponse {
    private String storeName;
    private String menuName;
    private String nickname;
    private int rating;
    private String contents;
    private LocalDateTime createdAt;

    private RegisterReviewResponse(String storeName, String menuName, String nickname, int rating, String contents, LocalDateTime createdAt) {
        this.storeName = storeName;
        this.menuName = menuName;
        this.nickname = nickname;
        this.rating = rating;
        this.contents = contents;
        this.createdAt = createdAt;
    }

    public static RegisterReviewResponse from(Review review) {
        return new RegisterReviewResponse(
                review.getStore().getName(),
                review.getOrder().getMenuName(),
                review.getUser().getNickname(),
                review.getRating(),
                review.getContents(),
                review.getCreatedAt()
        );
    }
}
