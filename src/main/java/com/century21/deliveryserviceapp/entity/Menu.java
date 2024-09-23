package com.century21.deliveryserviceapp.entity;

import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "menu_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Menu {
    // 필요한 필드 : 메뉴명, 가격, 연관된 가게,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String menuName;

    private int price;

    private String name;

    @Column
    @NotNull
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    private Menu(String name, int price, Store store) {
        this.name = name;
        this.price = price;
        this.store = store;
    }

    public static Menu from(MenuRequest menuRequest, Store store) {
        return new Menu(
                menuRequest.getName(),
                menuRequest.getPrice(),
                store
        );
    }

    public void updateMenu(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void deleteMenu() {
        this.deletedAt = LocalDateTime.now();
    }
}
