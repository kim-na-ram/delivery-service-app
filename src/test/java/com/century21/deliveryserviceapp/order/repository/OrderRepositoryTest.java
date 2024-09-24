package com.century21.deliveryserviceapp.order.repository;

import com.century21.deliveryserviceapp.common.config.PasswordEncoder;
import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.data.MenuMockDataUtil;
import com.century21.deliveryserviceapp.data.StoreMockDataUtil;
import com.century21.deliveryserviceapp.data.UserMockDataUtil;
import com.century21.deliveryserviceapp.entity.Menu;
import com.century21.deliveryserviceapp.entity.Order;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.menu.repository.MenuRepository;
import com.century21.deliveryserviceapp.order.enums.PaymentMethod;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.NOT_FOUND_ORDER;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
public class OrderRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderRepository orderRepository;

    private PasswordEncoder passwordEncoder;
    private User savedUser;
    private User savedOwner;
    private Menu savedMenu;
    private Store savedStore;

    @BeforeEach
    public void setUp() {
        passwordEncoder = new PasswordEncoder();
        User user = User.from(UserMockDataUtil.signUpRequestWithUser(), passwordEncoder);
        savedUser = userRepository.save(user);

        User owner = User.from(UserMockDataUtil.signUpRequestWithOwner("email1"), passwordEncoder);
        savedOwner = userRepository.save(owner);

        Store store = Store.from(savedOwner, StoreMockDataUtil.registerStoreRequest());
        savedStore = storeRepository.save(store);

        Menu menu = Menu.from(MenuMockDataUtil.menuRequest(10000), savedStore);
        savedMenu = menuRepository.save(menu);
    }

    @Nested
    @DisplayName("주문 조회 테스트 케이스")
    public class GetOrder {
        @Test
        @DisplayName("존재하지 않는 주문 고유번호로 인해 주문 조회에 실패한다.")
        public void findOrder_notFoundOrder_failure() {
            // given
            long orderId = 1L;

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderRepository.findByOrderId(orderId));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("취소된 주문은 조회할 수 없어 주문 조회에 실패한다.")
        public void findOrder_orderIsDeleted_failure() {
            // given
            Order order = Order.of(PaymentMethod.CARD, savedUser, savedStore, savedMenu);
            order.cancelOrder();

            Order deletedOrder = orderRepository.save(order);

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderRepository.findByOrderId(deletedOrder.getId()));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 조회에 성공한다.")
        public void findOrder_success() {
            // given
            Order order = Order.of(PaymentMethod.CARD, savedUser, savedStore, savedMenu);
            Order savedOrder = orderRepository.save(order);

            // when
            Order findOrder = orderRepository.findByOrderId(savedOrder.getId());

            // then
            assertEquals(savedOrder.getId(), findOrder.getId());
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트 케이스")
    public class ChangeOrderStatus {
        @Test
        @DisplayName("사장님 본인 가게의 주문이 아니어서 주문 상태 변경에 실패한다.")
        public void changeOrderStatus_notFoundOrder_failure() {
            // given
            User owner = User.from(UserMockDataUtil.signUpRequestWithOwner("email2"), passwordEncoder);
            User savedOwner = userRepository.save(owner);

            Store store = Store.from(savedOwner, StoreMockDataUtil.registerStoreRequest());
            savedStore = storeRepository.save(store);

            Order order = Order.of(PaymentMethod.CARD, savedUser, savedStore, savedMenu);
            Order savedOrder = orderRepository.save(order);

            // when
            Optional<Order> order1 = orderRepository.findActiveOrderByIdAndOwnerId(savedOrder.getId(), savedOwner.getId());

            // then
            assertTrue(order1.isPresent());
        }

        @Test
        @DisplayName("취소한 주문의 상태를 변경할 수 없어 주문 상태 변경에 실패한다.")
        public void changeOrderStatus_deletedOrderStatus_failure() {
            // given
            Order order = Order.of(PaymentMethod.CARD, savedUser, savedStore, savedMenu);
            order.cancelOrder();

            Order savedOrder = orderRepository.save(order);

            // when
            boolean isOrderPresent = orderRepository.findActiveOrderByIdAndOwnerId(savedOrder.getId(), savedOwner.getId()).isPresent();

            // then
            assertFalse(isOrderPresent);
        }

        @Test
        @DisplayName("주문 상태 변경에 성공한다.")
        public void changeOrderStatus_success() {
            // given
            Order order = Order.of(PaymentMethod.CARD, savedUser, savedStore, savedMenu);
            Order savedOrder = orderRepository.save(order);

            // when
            boolean isOrderPresent = orderRepository.findActiveOrderByIdAndOwnerId(savedOrder.getId(), savedOwner.getId()).isPresent();

            // then
            assertTrue(isOrderPresent);
        }
    }
}