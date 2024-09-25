package com.century21.deliveryserviceapp.data;

import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;

public class StoreMockDataUtil {
    public static RegisterStoreRequest registerStoreRequest() {
        return new RegisterStoreRequest("storeName", "introduction", LocalTime.of(0, 1), LocalTime.of(23, 59), 10000);
    }

    public static Store store() {
        User user = UserMockDataUtil.user();
        Store store = Store.from(user, registerStoreRequest());
        ReflectionTestUtils.setField(store, "id", 1L);

        return store;
    }

    public static Store storeIsNotOpening() {
        Store store = store();
        ReflectionTestUtils.setField(store, "openingTime", LocalTime.now().plusHours(1));
        ReflectionTestUtils.setField(store, "closedTime", LocalTime.now().plusHours(2));

        return store;
    }

    public static Store storeIsClosed() {
        Store store = store();
        ReflectionTestUtils.setField(store, "openingTime", LocalTime.now().minusHours(2));
        ReflectionTestUtils.setField(store, "closedTime", LocalTime.now().minusHours(1));

        return store;
    }
}
