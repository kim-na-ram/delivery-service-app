package com.century21.deliveryserviceapp.store.service;

import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.common.exception.ForbiddenException;
import com.century21.deliveryserviceapp.common.exception.InvalidParameterException;
import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.review.repository.ReviewRepository;
import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.request.UpdateStoreRequest;
import com.century21.deliveryserviceapp.store.dto.response.*;
import com.century21.deliveryserviceapp.store.repository.StoreRepository;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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

        //3개까지만 등록 가능
        int storeCount=storeRepository.findStoreCount(userId);
        if(storeCount>=3){
            throw new ForbiddenException(MAX_STORE_LIMIT);
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

    @Transactional
    public StoreDetailResponse getStore(Long storeId) {
        Store store=storeRepository.findById(storeId).orElseThrow(()->
                new NotFoundException(NOT_FOUND_STORE));

        //폐업 했는 지 확인
        if(store.getDeletedAt()!=null){
            throw new NotFoundException(NOT_FOUND_STORE);
        }

        //리뷰 평균 평점 계산하기
        double averageRating = reviewRepository.calculateAverageRating(storeId);
        store.setAverageRating(averageRating);
        storeRepository.save(store);

        List<MenuDto> menuDtoList=store.getMenuList().stream()
                .map(menu-> new MenuDto(menu.getMenuName(),menu.getPrice()))
                .collect(Collectors.toList());

        return new StoreDetailResponse(
                store.getName(),
                store.getIntroduction(),
                store.getOpeningTime(),
                store.getClosedTime(),
                store.getMinOrderPrice(),
                store.getAverageRating(),
                menuDtoList
        );
    }


    public Page<StoreResponse> getStores(String name, int pageSize, int pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber-1,pageSize);

        Page<Store> stores;
        if(name==null || name.isEmpty()){
            stores=storeRepository.findAll(pageable);
        }else{
            stores=storeRepository.findByNameContaining(name,pageable);
        }

        return stores.map(store->{
            return new StoreResponse(
                    store.getId(),
                    store.getName(),
                    store.getMinOrderPrice(),
                    store.getAverageRating()
            );
        });
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
