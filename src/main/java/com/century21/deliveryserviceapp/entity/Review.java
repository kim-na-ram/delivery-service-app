package com.century21.deliveryserviceapp.entity;

import com.century21.deliveryserviceapp.review.dto.request.RegisterReviewRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "review_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private int rating;

    @Column
    @NotNull
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Review(int rating, String contents, Order order, User user, Store store) {
        this.rating = rating;
        this.contents = contents;
        this.order = order;
        this.user = user;
        this.store = store;
    }

    public static Review from(RegisterReviewRequest reviewRequest, Order order, User user, Store store) {
        return new Review(
                reviewRequest.getRating(),
                reviewRequest.getContents(),
                order,
                user,
                store
        );
    }
}
