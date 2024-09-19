package com.century21.deliveryserviceapp.common.response;

import com.century21.deliveryserviceapp.common.exception.ApiException;
import com.century21.deliveryserviceapp.common.exception.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseResponse {

    private final int code;
    private final String message;

    public static BaseResponse from(ResponseCode responseCode) {
        return new BaseResponse(responseCode.getStatus().value(), responseCode.getMessage());
    }

    public static BaseResponse from(ApiException apiException) {
        return new BaseResponse(apiException.getCode(), apiException.getMessage());
    }
}
