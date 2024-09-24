package com.century21.deliveryserviceapp.data;

import com.century21.deliveryserviceapp.entity.Menu;
import com.century21.deliveryserviceapp.entity.Order;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.order.dto.request.ChangeOrderStatusRequest;
import com.century21.deliveryserviceapp.order.dto.request.OrderRequest;
import com.century21.deliveryserviceapp.order.enums.PaymentMethod;
import org.springframework.test.util.ReflectionTestUtils;

public class OrderMockDataUtil {
    public static OrderRequest orderRequest() {
        return new OrderRequest(1L, 1L, "카드");
    }

    public static OrderRequest orderRequestWithInvalidPaymentMethod() {
        return new OrderRequest(1L, 1L, "??");
    }

    public static ChangeOrderStatusRequest changeOrderStatusRequest() {
        return new ChangeOrderStatusRequest("배달 시작");
    }

    public static ChangeOrderStatusRequest changeOrderStatusRequestWithInvalidOrderStatus() {
        return new ChangeOrderStatusRequest("??");
    }

    public static ChangeOrderStatusRequest changeOrderStatusRequestWithPreviousOrderStatus() {
        return new ChangeOrderStatusRequest("수락 대기");
    }

    public static ChangeOrderStatusRequest changeOrderStatusRequestToCancel() {
        return new ChangeOrderStatusRequest("주문 취소");
    }

    public static Order orderWithoutId() {
        User user = UserMockDataUtil.user();
        Store store = StoreMockDataUtil.store();
        Menu menu = MenuMockDataUtil.menu(10000);
        return Order.of(PaymentMethod.CARD, user, store, menu);
    }

    public static Order order() {
        User user = UserMockDataUtil.user();
        Store store = StoreMockDataUtil.store();
        Menu menu = MenuMockDataUtil.menu(10000);
        Order order = Order.of(PaymentMethod.CARD, user, store, menu);
        ReflectionTestUtils.setField(order, "id", 1L);

        return order;
    }
}
