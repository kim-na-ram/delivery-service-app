package com.century21.deliveryserviceapp.store.service;


import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.response.RegisterStoreResponse;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public RegisterStoreResponse registerStore(Long userId,RegisterStoreRequest registerStoreRequest) {

        User user=userRepository.findById(userId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_USER));

        Store newStore=Store.from(user,registerStoreRequest);

        Store savedStore=storeRepository.save(newStore);

        return new RegisterStoreResponse(
                savedStore.getId(),
                savedStore.getName(),
                savedStore.getIntroduction(),
                savedStore.getOpeningTime(),
                savedStore.getClosedTime(),
                savedStore.getMinOrderPrice()
        );
    }
}
