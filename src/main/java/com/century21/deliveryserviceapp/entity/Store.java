package com.century21.deliveryserviceapp.entity;

import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Entity
@Table(name = "store_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String introduction;

    @Column(name = "opening_time")
    @NotNull
    private Time openingTime;

    @Column(name = "closed_time")
    @NotNull
    private Time closedTime;

    @Column(name = "min_order_price")
    @NotNull
    private int minOrderPrice;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    private Store(User user, String name, String introduction, Time openingTime, Time closedTime, int minOrderPrice) {
        this.user = user;
        this.name = name;
        this.introduction = introduction;
        this.openingTime = openingTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
    }

    public Store from(User user, RegisterStoreRequest registerStoreRequest) {
        return new Store(
                user,
                registerStoreRequest.getStoreName(),
                registerStoreRequest.getIntroduction(),
                Time.valueOf(registerStoreRequest.getOpeningTime()),
                Time.valueOf(registerStoreRequest.getClosedTime()),
                registerStoreRequest.getMinOrderPrice()
        );
    }

    public void deleteStore() {
        this.deletedAt = new Timestamp(System.currentTimeMillis());
    }
}
