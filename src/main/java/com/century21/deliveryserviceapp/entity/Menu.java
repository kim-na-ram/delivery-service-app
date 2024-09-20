package com.century21.deliveryserviceapp.entity;

import com.century21.deliveryserviceapp.menu.common.entity.Timestamped;
import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;
import jakarta.persistence.*;
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
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Menu extends Timestamped {
    // 필요한 필드 : 메뉴명, 가격, 연관된 가게,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;

    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    public Menu(String menuName, Long price, Store store) {
        this.menuName = menuName;
        this.price = price;
        this.store = store;
    }

    public static Menu from(MenuRequest menuRequest, Store store) {
        return new Menu(
                menuRequest.getMenuName(),
                menuRequest.getPrice(),
                store
        );
    }
}
