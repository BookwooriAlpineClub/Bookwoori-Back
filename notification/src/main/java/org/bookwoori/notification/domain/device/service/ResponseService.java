package org.bookwoori.notification.domain.device.service;


import org.bookwoori.notification.domain.device.dto.response.DataResponseDto;
import org.bookwoori.notification.domain.notification.dto.response.CommonResponseDto;
import org.bookwoori.notification.global.exception.CustomExceptionStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public CommonResponseDto getSuccessResponse() {

        return CommonResponseDto.builder()
                .isSuccess(true)
                .code(5000)
                .message("요청에 성공하였습니다.")
                .build();

    }

    public <T> DataResponseDto<Object> getDataResponse(T data) {

        return DataResponseDto.builder()
                .result(data)
                .isSuccess(true)
                .code(5000)
                .message("요청에 성공하였습니다.")
                .build();

    }

    public CommonResponseDto getExceptionResponse(CustomExceptionStatus status) {

        return CommonResponseDto.builder()
                .isSuccess(status.isSuccess())
                .code(status.getCode())
                .message(status.getMessage())
                .build();

    }
}
