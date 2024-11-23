package org.bookwoori.notification.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.notification.dto.request.RegisterRequest;
import org.bookwoori.notification.dto.response.CommonResponse;
import org.bookwoori.notification.dto.response.DataResponse;
import org.bookwoori.notification.dto.response.DeviceResponse;
import org.bookwoori.notification.service.DeviceService;
import org.bookwoori.notification.service.ResponseService;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/notification-server/device")
@RequiredArgsConstructor
public class DeviceController {

    private final ResponseService responseService;
    private final DeviceService deviceService;

    @GetMapping("/{id}")
    public DataResponse<DeviceResponse> getDevice(@PathVariable("id") Long userId) {
        log.info("GET /notification-server/device/{}", userId);
        DeviceResponse response = deviceService.getDevice(userId);
        return responseService.getDataResponse(response);
    }

    @PostMapping
    public CommonResponse register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /notification-server/device");
        deviceService.register(request);
        return responseService.getSuccessResponse();
    }

    @DeleteMapping({"/{id}"})
    public CommonResponse delete(@PathVariable("id") Long userId) {
        log.info("DELETE /notification-server/device/{}", userId);
        deviceService.delete(userId);
        return responseService.getSuccessResponse();
    }
}