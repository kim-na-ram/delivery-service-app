package com.century21.deliveryserviceapp.review.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterReviewRequest {
    @Min(value = 0, message = "별점은 최소 0점부터 줄 수 있습니다.")
    @Max(value = 5, message = "별점은 최대 5점까지 줄 수 있습니다.")
    @NotNull
    private Integer rating;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 255, message = "내용은 최대 255자까지 작성 가능합니다.")
    private String contents;

    @NotNull(message = "주문 고유번호는 필수입니다.")
    private Long orderId;
}
