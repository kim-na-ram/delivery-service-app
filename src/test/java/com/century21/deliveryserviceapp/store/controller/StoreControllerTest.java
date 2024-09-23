package com.century21.deliveryserviceapp.store.controller;

import com.century21.deliveryserviceapp.entity.Store;
import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.response.MenuResponse;
import com.century21.deliveryserviceapp.store.dto.response.RegisterStoreResponse;
import com.century21.deliveryserviceapp.store.dto.response.StoreDetailResponse;
import com.century21.deliveryserviceapp.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.swing.text.html.Option;

import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
class StoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private StoreController storeController;

    @MockBean
    private StoreService storeService;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    public void 가게_등록() throws Exception {
        //given
        long userId=1L;
        RegisterStoreRequest request=new RegisterStoreRequest(

        );

        RegisterStoreResponse response=new RegisterStoreResponse();
        given(storeService.registerStore(anyLong(),any(request.getClass()))).willReturn(response);

        //when
        ResultActions resultActions=mockMvc.perform(post("/api/stores/{userId}",userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(request)));

        //then

    }

    @Test
    public void 가게_단건_조회() throws Exception {
        //given
        long storeId=1L;

        StoreDetailResponse storeDetailResponse=new StoreDetailResponse();
        given(storeService.getStore(anyLong())).willReturn(storeDetailResponse);

        //when
        ResultActions resultActions=mockMvc.perform(get("/api/stores/{storeId}",storeId));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 가게_리스트_조회() throws Exception{
        //given

        //when

        //then
    }

}