package com.century21.deliveryserviceapp.common.response;

import com.century21.deliveryserviceapp.common.exception.ResponseCode;
import lombok.Getter;

@Getter
public class SuccessResponse<T> extends BaseResponse {
    private T data;

    private SuccessResponse(T data) {
        super(ResponseCode.SUCCESS.getStatus().value(), ResponseCode.SUCCESS.getMessage());
        this.data = data;
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(data);
    }
}
