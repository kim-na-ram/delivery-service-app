package com.century21.deliveryserviceapp.order.service;

import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.common.exception.InvalidParameterException;
import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.common.exception.ResponseCode;
import com.century21.deliveryserviceapp.common.exception.UnauthorizedException;
import com.century21.deliveryserviceapp.entity.Menu;
import com.century21.deliveryserviceapp.entity.Order;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.menu.repository.MenuRepository;
import com.century21.deliveryserviceapp.order.dto.request.ChangeOrderStatusRequest;
import com.century21.deliveryserviceapp.order.dto.request.OrderRequest;
import com.century21.deliveryserviceapp.order.dto.response.OrderListResponse;
import com.century21.deliveryserviceapp.order.dto.response.OrderResponse;
import com.century21.deliveryserviceapp.order.enums.OrderStatus;
import com.century21.deliveryserviceapp.order.enums.PaymentMethod;
import com.century21.deliveryserviceapp.order.repository.OrderRepository;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    public OrderResponse saveOrder(AuthUser authUser, OrderRequest orderRequest) {
        // 사용자만 주문 가능
        if (!checkAuthority(authUser.getAuthority(), Authority.USER)) {
            throw new UnauthorizedException(UNAUTHORIZED_ORDER);
        }

        User user = userRepository.findByUserId(authUser.getUserId());

        // storeId가 존재하는지 확인
        Store store = storeRepository.findByStoreId(orderRequest.getStoreId());

        // 영업중인지 확인
        checkStoreIsOpening(store);

        // 해당 가게에 해당 menuId가 존재하는지 확인
        Menu menu = menuRepository.findByMenuId(orderRequest.getMenuId(), orderRequest.getStoreId());

        // 최소 주문 금액보다 낮으면 주문 불가능
        checkIsOverMinOrderPrice(store.getMinOrderPrice(), menu.getPrice());

        PaymentMethod paymentMethod = PaymentMethod.getPaymentMethod(orderRequest.getPaymentMethod());

        Order order = Order.of(paymentMethod, user, store, menu);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderListResponse> getOrderList(AuthUser authUser) {
        List<OrderListResponse> orderList;

        if (checkAuthority(authUser.getAuthority(), Authority.OWNER)) {
            orderList = OrderListResponse.from(orderRepository.findAllByOwnerId(authUser.getUserId()));
        } else {
            orderList = OrderListResponse.from(orderRepository.findAllByUserId(authUser.getUserId()));
        }

        return orderList;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(AuthUser authUser, long orderId) {
        Order order;

        if (checkAuthority(authUser.getAuthority(), Authority.OWNER)) {
            order = orderRepository.findByIdAndOwnerId(orderId, authUser.getUserId())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
        } else {
            order = orderRepository.findByOrderIdAndUserId(orderId, authUser.getUserId());
        }

        return OrderResponse.from(order);
    }

    public OrderResponse changeOrderStatus(AuthUser authUser, long orderId, ChangeOrderStatusRequest changeOrderStatusRequest) {
        // 사장님만 주문 상태 변경 가능
        if (!checkAuthority(authUser.getAuthority(), Authority.OWNER)) {
            throw new UnauthorizedException(ResponseCode.UNAUTHORIZED_CHANGE_ORDER_STATUS);
        }

        // 변경하고자 하는 orderStatus
        OrderStatus changeStatus = OrderStatus.getStatus(changeOrderStatusRequest.getOrderStatus());

        Order order = findOrderWithOwnerId(orderId, authUser.getUserId());

        // 주문 상태 변경 전 확인
        checkIsNextStatus(order.getStatus(), changeStatus);

        // 주문 상태 변경
        order.changeOrderStatus(changeStatus);

        return OrderResponse.from(order);
    }

    public OrderResponse deleteOrder(AuthUser authUser, long orderId) {
        // 주문 확인
        Order order;

        if (checkAuthority(authUser.getAuthority(), Authority.OWNER)) {
            order = findOrderWithOwnerId(orderId, authUser.getUserId());
        } else {
            order = findOrderWithUserId(orderId, authUser.getUserId());
        }

        // 주문 상태 = 수락 대기가 아니면 취소 불가능
        checkIsPossibleToCancelOrder(order.getStatus(), authUser.getAuthority());

        // 주문 취소 처리
        order.cancelOrder();
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    /**
     * 권한 확인
     *
     * @param authorityName 현재 유저의 권한
     * @param authority     비교하고자 하는 권한
     */
    private boolean checkAuthority(String authorityName, Authority authority) {
        return authorityName.equals(authority.name());
    }

    /**
     * 가게가 영업중인지 확인한다.
     *
     * @param store 확인이 필요한 가게
     */
    private void checkStoreIsOpening(Store store) {
        LocalTime now = LocalTime.now();

        if (now.isBefore(store.getOpeningTime())
                || now.isAfter(store.getClosedTime())) {
            throw new InvalidParameterException(INVALID_ORDER_TIME);
        }
    }

    /**
     * 사장님 본인 가게의 주문인지 확인한다.
     *
     * @param orderId 주문 고유번호
     * @param ownerId 사장님 고유번호
     * @return 조회된 주문
     */
    private Order findOrderWithOwnerId(long orderId, long ownerId) {
        return orderRepository.findActiveOrderByIdAndOwnerId(orderId, ownerId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
    }

    /**
     * 사용자의 주문인지 확인한다.
     *
     * @param orderId 주문 고유번호
     * @param userId  사용자 고유번호
     * @return 조회된 주문
     */
    private Order findOrderWithUserId(long orderId, long userId) {
        return orderRepository.findActiveOrderByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
    }

    /**
     * 주문이 취소 가능한지 확인한다.
     *
     * @param orderStatus 현재 주문 상태
     * @param authority   요청한 유저의 권한
     */
    private void checkIsPossibleToCancelOrder(OrderStatus orderStatus, String authority) {
        if (orderStatus.equals(OrderStatus.PENDING_ACCEPTANCE)) return;

        if (checkAuthority(authority, Authority.OWNER)) {
            throw new InvalidParameterException(ResponseCode.INVALID_CANCEL_ORDER_FOR_OWNER);
        } else {
            throw new InvalidParameterException(ResponseCode.INVALID_CANCEL_ORDER_FOR_USER);
        }
    }

    /**
     * 주문 금액이 최소 주문 금액을 넘는지 확인한다.
     *
     * @param minOrderPrice 최소 주문 금액
     * @param orderAmount   주문 금액
     */
    private void checkIsOverMinOrderPrice(int minOrderPrice, int orderAmount) {
        if (orderAmount < minOrderPrice) {
            throw new InvalidParameterException(ResponseCode.INVALID_MINIMAL_ORDER_PRICE);
        }
    }

    /**
     * 변경하려는 주문 상태가 현재의 주문 상태보다 다음 단계인지 확인한다.
     *
     * @param nowStatus    현재 주문 상태
     * @param changeStatus 변경하려는 주문 상태
     */
    private void checkIsNextStatus(OrderStatus nowStatus, OrderStatus changeStatus) {
        int nowStep = nowStatus.getStep();
        int changeStep = changeStatus.getStep();

        if (nowStep > changeStep) {
            throw new InvalidParameterException(ResponseCode.INVALID_CHANGE_ORDER_STATUS);
        } else if (nowStep == changeStep) {
            throw new InvalidParameterException(INVALID_CHANGE_SAME_ORDER_STATUS);
        }

        if (changeStatus.equals(OrderStatus.CANCELED)) {
            throw new InvalidParameterException(INVALID_CHANGE_ORDER_STATUS_TO_CANCEL);
        }
    }
}
