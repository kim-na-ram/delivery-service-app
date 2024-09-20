package com.century21.deliveryserviceapp.menu.service;


import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.menu.dto.request.MenuRequest;
import com.century21.deliveryserviceapp.menu.dto.response.MenuResponse;
import com.century21.deliveryserviceapp.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;


//    public MenuResponse saveMenu(Long storeId, MenuRequest menuRequest) {
//
//    }
}
