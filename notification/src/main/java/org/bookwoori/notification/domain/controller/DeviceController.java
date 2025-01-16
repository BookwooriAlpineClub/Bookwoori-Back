package org.bookwoori.notification.domain.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.notification.domain.dto.request.RegisterRequestDto;
import org.bookwoori.notification.domain.dto.response.CommonResponseDto;
import org.bookwoori.notification.domain.dto.response.DataResponseDto;
import org.bookwoori.notification.domain.dto.response.DeviceResponseDto;
import org.bookwoori.notification.domain.service.DeviceService;
import org.bookwoori.notification.domain.service.MemberService;
import org.bookwoori.notification.domain.service.ResponseService;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@Tag(name = "Notification(Device)")
@RequestMapping("/notification/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final ResponseService responseService;
    private final DeviceService deviceService;
    private final MemberService memberService;

    @Operation(summary = "등록 정보 조회", description = "등록 정보를 조회합니다.")
    @GetMapping
    public DataResponseDto<Object> getDevice(HttpServletRequest request) {
        Long userId = memberService.getCurrentMemberId(request);
        DeviceResponseDto response = deviceService.getDevice(userId);
        return responseService.getDataResponse(response);
    }

    @Operation(summary = "기기 등록", description = "기기를 등록합니다.")
    @PostMapping
    public CommonResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
        deviceService.register(request);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "기기 삭제", description = "기기를 삭제합니다.")
    @DeleteMapping
    public CommonResponseDto delete(HttpServletRequest request) {
        Long userId = memberService.getCurrentMemberId(request);
        deviceService.delete(userId);
        return responseService.getSuccessResponse();
    }
}