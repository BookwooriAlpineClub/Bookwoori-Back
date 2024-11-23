package org.bookwoori.notification.service;


import org.bookwoori.notification.dto.response.CommonResponse;
import org.bookwoori.notification.dto.response.DataResponse;
import org.bookwoori.notification.exception.CustomExceptionStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public CommonResponse getSuccessResponse() {
        CommonResponse response = new CommonResponse();
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("요청에 성공하였습니다.");
        return response;
    }

    public <T> DataResponse<T> getDataResponse(T data) {
        DataResponse<T> response = new DataResponse<>();
        response.setResult(data);
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("요청에 성공하였습니다.");
        return response;
    }

    public CommonResponse getExceptionResponse(CustomExceptionStatus status) {
        CommonResponse response = new CommonResponse();
        response.setIsSuccess(status.isSuccess());
        response.setCode(status.getCode());
        response.setMessage(status.getMessage());
        return response;
    }
}
