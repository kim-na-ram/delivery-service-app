package com.century21.deliveryserviceapp.order.service;

import com.century21.deliveryserviceapp.user.auth.AuthUser;
import com.century21.deliveryserviceapp.common.exception.InvalidParameterException;
import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.common.exception.UnauthorizedException;
import com.century21.deliveryserviceapp.data.AuthUserMockUtil;
import com.century21.deliveryserviceapp.data.MenuMockDataUtil;
import com.century21.deliveryserviceapp.data.OrderMockDataUtil;
import com.century21.deliveryserviceapp.data.StoreMockDataUtil;
import com.century21.deliveryserviceapp.entity.Menu;
import com.century21.deliveryserviceapp.entity.Order;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.menu.repository.MenuRepository;
import com.century21.deliveryserviceapp.order.dto.request.ChangeOrderStatusRequest;
import com.century21.deliveryserviceapp.order.dto.request.OrderRequest;
import com.century21.deliveryserviceapp.order.dto.response.OrderListResponse;
import com.century21.deliveryserviceapp.order.dto.response.OrderResponse;
import com.century21.deliveryserviceapp.order.enums.OrderStatus;
import com.century21.deliveryserviceapp.order.repository.OrderRepository;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Nested
    @DisplayName("주문 테스트 케이스")
    public class SaveOrder {
        @Test
        @DisplayName("사장님은 주문을 할 수 없기 때문에 주문에 실패한다.")
        public void saveOrder_unauthorizedOwnerOrder_failure() {
            // given
            AuthUser owner = AuthUserMockUtil.ownerAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequest();

            // when
            Throwable t = assertThrows(UnauthorizedException.class, () -> orderService.saveOrder(owner, orderRequest));

            // then
            assertEquals(UNAUTHORIZED_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 유저는 주문이 불가능해 주문에 실패한다.")
        void saveOrder_notFoundUser_failure() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequest();

            given(userRepository.findByUserId(anyLong())).willThrow(new NotFoundException(NOT_FOUND_USER));

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderService.saveOrder(user, orderRequest));

            // then
            assertEquals(NOT_FOUND_USER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 가게에는 주문이 불가능해 주문에 실패한다.")
        void saveOrder_notFoundStore_failure() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequest();

            given(storeRepository.findByStoreId(anyLong())).willThrow(new NotFoundException(NOT_FOUND_STORE));

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderService.saveOrder(user, orderRequest));

            // then
            assertEquals(NOT_FOUND_STORE.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("가게가 영업을 시작하기 전이라 주문에 실패한다.")
        void saveOrder_storeIsNotOpening_failure() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequest();

            Store store = StoreMockDataUtil.storeIsNotOpening();

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.saveOrder(user, orderRequest));

            // then
            assertEquals(INVALID_ORDER_TIME.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("가게가 영업을 마감해 주문에 실패한다.")
        void saveOrder_storeIsClosed_failure() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequest();

            Store store = StoreMockDataUtil.storeIsClosed();

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.saveOrder(user, orderRequest));

            // then
            assertEquals(INVALID_ORDER_TIME.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 메뉴는 주문이 불가능해 주문에 실패한다.")
        void saveOrder_notFoundMenu_failure() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequest();

            Store store = StoreMockDataUtil.store();

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);
            given(menuRepository.findByMenuId(anyLong(), anyLong())).willThrow(new NotFoundException(NOT_FOUND_MENU));

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderService.saveOrder(user, orderRequest));

            // then
            assertEquals(NOT_FOUND_MENU.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("최소 주문 금액보다 낮으면 주문이 불가능해 주문에 실패한다.")
        void saveOrder_underMinOrderPrice_failure() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequest();

            Store store = StoreMockDataUtil.store();
            Menu menu = MenuMockDataUtil.menu(1000);

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);
            given(menuRepository.findByMenuId(anyLong(), anyLong())).willReturn(menu);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.saveOrder(user, orderRequest));

            // then
            assertEquals(INVALID_MINIMAL_ORDER_PRICE.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("결제 방법이 잘못되어 주문에 실패한다.")
        public void saveOrder_invalidPaymentMethod_failure() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequestWithInvalidPaymentMethod();

            Store store = StoreMockDataUtil.store();
            Menu menu = MenuMockDataUtil.menu(10000);

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);
            given(menuRepository.findByMenuId(anyLong(), anyLong())).willReturn(menu);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.saveOrder(user, orderRequest));

            // then
            assertEquals(INVALID_PAYMENT_METHOD.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문에 성공한다.")
        public void saveOrder_success() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();
            OrderRequest orderRequest = OrderMockDataUtil.orderRequest();

            Store store = StoreMockDataUtil.store();
            Menu menu = MenuMockDataUtil.menu(10000);
            Order order = OrderMockDataUtil.order();

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);
            given(menuRepository.findByMenuId(anyLong(), anyLong())).willReturn(menu);
            given(orderRepository.save(any())).willReturn(order);

            // when
            OrderResponse orderResponse = orderService.saveOrder(user, orderRequest);

            // then
            assertEquals(order.getId(), orderResponse.getOrderId());
        }
    }

    @Nested
    @DisplayName("주문 목록 테스트 케이스")
    public class GetOrderList {
        @Test
        @DisplayName("사장님이 주문 목록 조회에 성공한다.")
        public void getOrderList_ownerOrderList_success() {
            // given
            AuthUser owner = AuthUserMockUtil.ownerAuth();

            Order order = OrderMockDataUtil.order();
            List<Order> orderList = List.of(order);

            given(orderRepository.findAllByOwnerId(anyLong())).willReturn(orderList);

            // when
            List<OrderListResponse> result = orderService.getOrderList(owner);

            // then
            assertEquals(orderList.size(), result.size());
        }

        @Test
        @DisplayName("사용자가 주문 목록 조회에 성공한다.")
        public void getOrderList_userOrderList_success() {
            // given
            AuthUser user = AuthUserMockUtil.userAuth();

            Order order = OrderMockDataUtil.order();
            List<Order> orderList = List.of(order);

            given(orderRepository.findAllByUserId(anyLong())).willReturn(orderList);

            // when
            List<OrderListResponse> result = orderService.getOrderList(user);

            // then
            assertEquals(orderList.size(), result.size());
        }
    }

    @Nested
    @DisplayName("주문 상세 테스트 케이스")
    public class GetOrder {
        @Test
        @DisplayName("사장님이 주문한 주문이 존재하지 않아 주문 상세에 실패한다.")
        public void getOrder_notFoundOrderWithOwnerId_failure() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();

            given(orderRepository.findByIdAndOwnerId(anyLong(), anyLong())).willReturn(Optional.empty());

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderService.getOrder(owner, orderId));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("사용자가 주문한 주문이 존재하지 않아 주문 상세에 실패한다.")
        public void getOrder_notFoundOrderWithUserId_failure() {
            // given
            long orderId = 1L;
            AuthUser user = AuthUserMockUtil.userAuth();

            given(orderRepository.findByOrderIdAndUserId(anyLong(), anyLong()))
                    .willThrow(new NotFoundException(NOT_FOUND_ORDER));

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderService.getOrder(user, orderId));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 상세에 성공한다.")
        public void getOrder_success() {
            // given
            long orderId = 1L;
            AuthUser user = AuthUserMockUtil.userAuth();

            Order order = OrderMockDataUtil.order();

            given(orderRepository.findByOrderIdAndUserId(anyLong(), anyLong())).willReturn(order);

            // when
            OrderResponse orderResponse = orderService.getOrder(user, orderId);

            // then
            assertEquals(order.getId(), orderResponse.getOrderId());
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트 케이스")
    public class ChangeOrderStatus {
        @Test
        @DisplayName("사장님이 아니면 주문 상태를 변경할 수 없다.")
        public void changeOrderStatus_unauthorizedOwnerOrder_failure() {
            // given
            long orderId = 1L;
            AuthUser user = AuthUserMockUtil.userAuth();
            ChangeOrderStatusRequest changeOrderStatusRequest = OrderMockDataUtil.changeOrderStatusRequest();

            // when
            Throwable t = assertThrows(UnauthorizedException.class,
                    () -> orderService.changeOrderStatus(user, orderId, changeOrderStatusRequest));

            // then
            assertEquals(UNAUTHORIZED_CHANGE_ORDER_STATUS.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 상태가 잘못되어 주문을 상태를 변경할 수 없다.")
        public void changeOrderStatus_invalidOrderStatus_failure() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();
            ChangeOrderStatusRequest changeOrderStatusRequest =
                    OrderMockDataUtil.changeOrderStatusRequestWithInvalidOrderStatus();

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> orderService.changeOrderStatus(owner, orderId, changeOrderStatusRequest));

            // then
            assertEquals(INVALID_ORDER_STATUS.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 주문의 상태는 변경할 수 없어 주문 상태 변경에 실패한다.")
        public void changeOrderStatus_notFoundOrder_failure() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();
            ChangeOrderStatusRequest changeOrderStatusRequest = OrderMockDataUtil.changeOrderStatusRequest();

            given(orderRepository.findActiveOrderByIdAndOwnerId(anyLong(), anyLong()))
                    .willThrow(new NotFoundException(NOT_FOUND_ORDER));

            // when
            Throwable t = assertThrows(NotFoundException.class,
                    () -> orderService.changeOrderStatus(owner, orderId, changeOrderStatusRequest));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("이전 주문 상태로 돌아갈 수 없기 때문에 주문 상태 변경에 실패한다.")
        public void changeOrderStatus_disableBackToPreviousOrderStatus_failure() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();
            ChangeOrderStatusRequest changeOrderStatusRequest = OrderMockDataUtil.changeOrderStatusRequestWithPreviousOrderStatus();

            Order order = OrderMockDataUtil.order();
            ReflectionTestUtils.setField(order, "status", OrderStatus.COOKING);

            given(orderRepository.findActiveOrderByIdAndOwnerId(anyLong(), anyLong())).willReturn(Optional.of(order));

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> orderService.changeOrderStatus(owner, orderId, changeOrderStatusRequest));

            // then
            assertEquals(INVALID_CHANGE_ORDER_STATUS.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("현재와 같은 주문 상태로 변경할 수 없어 주문 상태 변경에 실패한다.")
        public void changeOrderStatus_sameOrderStatus_failure() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();
            ChangeOrderStatusRequest changeOrderStatusRequest = OrderMockDataUtil.changeOrderStatusRequestWithPreviousOrderStatus();

            Order order = OrderMockDataUtil.order();

            given(orderRepository.findActiveOrderByIdAndOwnerId(anyLong(), anyLong())).willReturn(Optional.of(order));

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> orderService.changeOrderStatus(owner, orderId, changeOrderStatusRequest));

            // then
            assertEquals(INVALID_CHANGE_SAME_ORDER_STATUS.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 상태 변경으로는 주문을 취소할 수 없기 때문에 주문 상태 변경에 실패한다.")
        public void changeOrderStatus_disableChangeOrderStatusToCancel_failure() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();
            ChangeOrderStatusRequest changeOrderStatusRequest = OrderMockDataUtil.changeOrderStatusRequestToCancel();

            Order order = OrderMockDataUtil.order();

            given(orderRepository.findActiveOrderByIdAndOwnerId(anyLong(), anyLong())).willReturn(Optional.of(order));

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> orderService.changeOrderStatus(owner, orderId, changeOrderStatusRequest));

            // then
            assertEquals(INVALID_CHANGE_ORDER_STATUS_TO_CANCEL.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 상태 변경에 성공한다.")
        public void changeOrderStatus_success() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();
            ChangeOrderStatusRequest changeOrderStatusRequest = OrderMockDataUtil.changeOrderStatusRequest();

            Order order = OrderMockDataUtil.order();

            given(orderRepository.findActiveOrderByIdAndOwnerId(anyLong(), anyLong())).willReturn(Optional.of(order));

            // when
            OrderResponse orderResponse = orderService.changeOrderStatus(owner, orderId, changeOrderStatusRequest);

            // then
            assertEquals(changeOrderStatusRequest.getOrderStatus(), orderResponse.getOrderStatus());
        }

    }

    @Nested
    @DisplayName("주문 취소 테스트 케이스")
    public class DeleteOrderStatus {
        @Test
        @DisplayName("사장님이 취소하려는 주문이 존재하지 않아 주문 취소에 실패한다.")
        void deleteOrder_notFoundOrderWithOwnerId_failure() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();

            given(orderRepository.findActiveOrderByIdAndOwnerId(anyLong(), anyLong())).willReturn(Optional.empty());

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderService.deleteOrder(owner, orderId));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("사용자가 취소하려는 주문이 존재하지 않아 주문 취소에 실패한다.")
        void deleteOrder_notFoundOrderWithUserId_failure() {
            // given
            long orderId = 1L;
            AuthUser user = AuthUserMockUtil.userAuth();

            given(orderRepository.findActiveOrderByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderService.deleteOrder(user, orderId));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("사용자가 취소하려는 주문의 상태가 수락 대기가 아니어서 주문 취소에 실패한다.")
        void deleteOrder_orderStatusIsNotPendingAcceptanceFromUser_failure() {
            // given
            long orderId = 1L;
            AuthUser user = AuthUserMockUtil.userAuth();

            Order order = OrderMockDataUtil.order();
            ReflectionTestUtils.setField(order, "status", OrderStatus.COOKING);

            given(orderRepository.findActiveOrderByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(order));

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.deleteOrder(user, orderId));

            // then
            assertEquals(INVALID_CANCEL_ORDER_FOR_USER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("사장님이 취소하려는 주문의 상태가 수락 대기가 아니어서 주문 취소에 실패한다.")
        void deleteOrder_orderStatusIsNotPendingAcceptanceFromOwner_failure() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();

            Order order = OrderMockDataUtil.order();
            ReflectionTestUtils.setField(order, "status", OrderStatus.COOKING);

            given(orderRepository.findActiveOrderByIdAndOwnerId(anyLong(), anyLong())).willReturn(Optional.of(order));

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.deleteOrder(owner, orderId));

            // then
            assertEquals(INVALID_CANCEL_ORDER_FOR_OWNER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 취소에 성공한다.")
        void deleteOrder_success() {
            // given
            long orderId = 1L;
            AuthUser owner = AuthUserMockUtil.ownerAuth();

            Order order = OrderMockDataUtil.order();

            given(orderRepository.findActiveOrderByIdAndOwnerId(anyLong(), anyLong())).willReturn(Optional.of(order));
            given(orderRepository.save(any())).willReturn(order);

            // when
            OrderResponse orderResponse = orderService.deleteOrder(owner, orderId);

            // then
            assertEquals(OrderStatus.CANCELED.getStatus(), orderResponse.getOrderStatus());
        }
    }
}
