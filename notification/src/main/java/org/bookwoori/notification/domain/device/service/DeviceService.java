package org.bookwoori.notification.domain.device.service;


import lombok.RequiredArgsConstructor;
import org.bookwoori.notification.domain.device.entity.Device;
import org.bookwoori.notification.domain.device.entity.Platform;
import org.bookwoori.notification.domain.device.repository.DeviceRepository;
import org.bookwoori.notification.domain.device.dto.response.DeviceResponseDto;
import org.bookwoori.notification.global.exception.CustomException;
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
    public void register(Long memberId, Platform platform, String token) {
        Optional<Device> device = deviceRepository.findBymemberId(memberId);

        if (device.isPresent()) {
            device.get().renew(platform, token);
            return;
        }

        Device newDevice = Device.register(memberId, platform, token);
        deviceRepository.save(newDevice);
    }


    @Transactional
    public void delete(Long userId) {
        Device device = deviceRepository.findBymemberId(userId)
                .orElseThrow(() -> new CustomException(EMPTY_DEVICE));
        device.delete();
    }
}
