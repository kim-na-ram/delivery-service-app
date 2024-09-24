package com.century21.deliveryserviceapp.store.controller;

import com.century21.deliveryserviceapp.store.dto.request.RegisterStoreRequest;
import com.century21.deliveryserviceapp.store.dto.request.UpdateStoreRequest;
import com.century21.deliveryserviceapp.store.dto.response.*;
import com.century21.deliveryserviceapp.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.time.LocalTime;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        long userId=3L;
        RegisterStoreRequest request=new RegisterStoreRequest(
                "신전떡볶이",
                "떡볶이",
                LocalTime.of(10,0),
                LocalTime.of(20,0),
                10000
        );
        RegisterStoreResponse response=new RegisterStoreResponse();
        given(storeService.registerStore(anyLong(),any(request.getClass()))).willReturn(response);

        //when
        ResultActions resultActions=mockMvc.perform(post("/api/stores/{userId}",userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data").isNotEmpty());
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
        resultActions.andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    public void 가게_리스트_조회() throws Exception{
        //given
        StoreResponse storeResponse=new StoreResponse();
        Page<StoreResponse> pageResponse=new PageImpl<>(Collections.singletonList(storeResponse));
        given(storeService.getStores(anyString(),anyInt(),anyInt())).willReturn(pageResponse);

        //when
        ResultActions resultActions=mockMvc.perform(get("/api/stores")
                .param("name","Test")
                .param("pageSize","10")
                .param("pageNumber","1"));
        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.content").isNotEmpty());
    }

    @Test
    public void 가게_수정() throws Exception{
        //given
        long storeId=1L;
        long userId=3L;
        UpdateStoreRequest request=new UpdateStoreRequest(
                "신전떡볶이",
                "떡볶이",
                LocalTime.of(7,0),
                LocalTime.of(18,0),
                10000
        );
        UpdateStoreResponse response=new UpdateStoreResponse();
        given(storeService.updateStore(anyLong(),anyLong(),any(request.getClass()))).willReturn(response);

        //when
        ResultActions resultActions=mockMvc.perform(patch("/api/stores/{storeId}/{userId}",storeId,userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    public void 가게_폐업() throws Exception{
        //given
        long storeId=1L;
        doNothing().when(storeService).deleteStore(anyLong());
        //when
        ResultActions resultActions=mockMvc.perform(delete("/api/stores/{storeId}",storeId));
        //then
        resultActions.andExpect(status().isOk());
    }

}