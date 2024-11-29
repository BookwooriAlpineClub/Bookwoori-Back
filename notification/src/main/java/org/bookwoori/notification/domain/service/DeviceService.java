package org.bookwoori.notification.domain.service;


import lombok.RequiredArgsConstructor;
import org.bookwoori.notification.domain.Device;
import org.bookwoori.notification.domain.dto.request.RegisterRequestDto;
import org.bookwoori.notification.domain.dto.response.DeviceResponseDto;
import org.bookwoori.notification.global.exception.CustomException;
import org.bookwoori.notification.domain.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.bookwoori.notification.global.exception.CustomExceptionStatus.EMPTY_DEVICE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceResponseDto getDevice(Long userId) {
        Device device = deviceRepository.findBymemberId(userId)
                .orElseThrow(() -> new CustomException(EMPTY_DEVICE));
        return DeviceResponseDto.from(device);
    }

    @Transactional
    public void register(RegisterRequestDto request) {
        Optional<Device> device = deviceRepository.findBymemberId(request.memberId());

        if (device.isPresent()) {
            device.get().renew(request.platform(), request.token());
            return;
        }

        Device newDevice = Device.register(request.memberId(), request.platform(), request.token());
        deviceRepository.save(newDevice);
    }


    @Transactional
    public void delete(Long userId) {
        Device device = deviceRepository.findBymemberId(userId)
                .orElseThrow(() -> new CustomException(EMPTY_DEVICE));
        device.delete();
    }
}
