package com.century21.deliveryserviceapp.entity;

import com.century21.deliveryserviceapp.order.enums.OrderStatus;
import com.century21.deliveryserviceapp.order.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "order_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "payment_method")
    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    @Column(name = "menu_name")
    @NotNull
    private String menuName;

    @Column(name = "menu_price")
    @NotNull
    private int menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Order(PaymentMethod paymentMethod, String menuName, int menuPrice, User user, Store store) {
        this.status = OrderStatus.PENDING_ACCEPTANCE;
        this.paymentMethod = paymentMethod;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.user = user;
        this.store = store;
    }

    public static Order of(PaymentMethod paymentMethod, User user, Store store, Menu menu) {
        return new Order(paymentMethod, menu.getName(), menu.getPrice(), user, store);
    }

    public void cancelOrder() {
        this.status = OrderStatus.CANCELED;
        this.deletedAt = LocalDateTime.now();
    }

    public void changeOrderStatus(OrderStatus status) {
        this.status = status;
    }
}
