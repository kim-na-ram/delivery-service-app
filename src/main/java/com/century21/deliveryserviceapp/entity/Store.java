package com.century21.deliveryserviceapp.entity;

import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.request.UpdateStoreRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;

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
    private String introduction;

    @Column(name = "opening_time")
    @NotNull
    private LocalTime openingTime;

    @Column(name = "closed_time")
    @NotNull
    private LocalTime closedTime;

    @Column(name = "min_order_price")
    @NotNull
    private int minOrderPrice;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    private Store(User user, String name, String introduction, LocalTime openingTime, LocalTime closedTime, int minOrderPrice) {
        this.user = user;
        this.name = name;
        this.introduction = introduction;
        this.openingTime = openingTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
    }

    public static Store from(User user, RegisterStoreRequest registerStoreRequest) {
        return new Store(
                user,
                registerStoreRequest.getStoreName(),
                registerStoreRequest.getIntroduction(),
                registerStoreRequest.getOpeningTime(),
                registerStoreRequest.getClosedTime(),
                registerStoreRequest.getMinOrderPrice()
        );
    }

    public void deleteStore() {
        this.deletedAt = new Timestamp(System.currentTimeMillis());
    }

    public void update(UpdateStoreRequest updateStoreRequest){
        if(updateStoreRequest.getStoreName()!=null){
            this.name= updateStoreRequest.getStoreName();
        }
        if(updateStoreRequest.getIntroduction()!=null){
            this.introduction= updateStoreRequest.getIntroduction();
        }
        if(updateStoreRequest.getOpeningTime()!=null){
            this.openingTime=updateStoreRequest.getOpeningTime();
        }
        if(updateStoreRequest.getClosedTime()!=null){
            this.closedTime=updateStoreRequest.getClosedTime();
        }
        if(updateStoreRequest.getMinOrderPrice()!=null){
            this.minOrderPrice= updateStoreRequest.getMinOrderPrice();
        }
    }
}
