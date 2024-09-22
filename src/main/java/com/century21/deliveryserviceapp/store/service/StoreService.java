package com.century21.deliveryserviceapp.store.service;

import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.common.exception.InvalidParameterException;
import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.review.repository.ReviewRepository;
import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.request.UpdateStoreRequest;
import com.century21.deliveryserviceapp.store.dto.response.StoreDetailResponse;
import com.century21.deliveryserviceapp.store.dto.response.RegisterStoreResponse;
import com.century21.deliveryserviceapp.store.dto.response.UpdateStoreResponse;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public RegisterStoreResponse registerStore(Long userId,RegisterStoreRequest registerStoreRequest) {
        //user가 존재하는 지 확인
        User user=userRepository.findById(userId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_USER));

        //user의 권한이 OWNER인지 확인
        if(user.getAuthority()!= Authority.OWNER){
            throw new InvalidParameterException(INVALID_USER_AUTHORITY);
        }

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


    public StoreDetailResponse getStore(Long storeId) {
        Store store=storeRepository.findById(storeId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_STORE));

        return new StoreDetailResponse(
                store.getName(),
                store.getIntroduction(),
                store.getOpeningTime(),
                store.getClosedTime(),
                store.getMinOrderPrice(),
                store.getAverageRating(),
                store.getMenuList()
        );
    }


    @Transactional
    public UpdateStoreResponse updateStore(Long storeId, UpdateStoreRequest updateStoreRequest) {
        Store store=storeRepository.findById(storeId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_STORE));

        store.update(updateStoreRequest);

        return new UpdateStoreResponse(
                store.getId(),
                store.getName(),
                store.getIntroduction(),
                store.getOpeningTime(),
                store.getClosedTime(),
                store.getMinOrderPrice()
        );

    }

    @Transactional
    public void deleteStore(Long storeId) {
        Store store=storeRepository.findById(storeId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_STORE));

        store.deleteStore();

    }


}
