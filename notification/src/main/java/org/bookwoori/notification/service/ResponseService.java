package org.bookwoori.notification.service;


import org.bookwoori.notification.dto.response.CommonResponseDto;
import org.bookwoori.notification.dto.response.DataResponseDto;
import org.bookwoori.notification.exception.CustomExceptionStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public CommonResponseDto getSuccessResponse() {
        CommonResponseDto response = new CommonResponseDto();
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("요청에 성공하였습니다.");
        return response;
    }

    public <T> DataResponseDto<T> getDataResponse(T data) {
        DataResponseDto<T> response = new DataResponseDto<>();
        response.setResult(data);
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("요청에 성공하였습니다.");
        return response;
    }

    public CommonResponseDto getExceptionResponse(CustomExceptionStatus status) {
        CommonResponseDto response = new CommonResponseDto();
        response.setIsSuccess(status.isSuccess());
        response.setCode(status.getCode());
        response.setMessage(status.getMessage());
        return response;
    }
}
