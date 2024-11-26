package org.bookwoori.notification.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Notification(Device)")
@RequestMapping("/notification-server/device")
@RequiredArgsConstructor
public class DeviceController {

    private final ResponseService responseService;
    private final DeviceService deviceService;

    @Operation(summary = "등록 정보 조회", description = "등록 정보를 조회합니다.")
    @GetMapping("/{id}")
    public DataResponse<DeviceResponse> getDevice(@PathVariable("id") Long userId) {
        DeviceResponse response = deviceService.getDevice(userId);
        return responseService.getDataResponse(response);
    }

    @Operation(summary = "기기 등록", description = "기기를 등록합니다.")
    @PostMapping
    public CommonResponse register(@Valid @RequestBody RegisterRequest request) {
        deviceService.register(request);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "기기 삭제", description = "기기를 삭제합니다.")
    @DeleteMapping({"/{id}"})
    public CommonResponse delete(@PathVariable("id") Long userId) {
        deviceService.delete(userId);
        return responseService.getSuccessResponse();
    }
}