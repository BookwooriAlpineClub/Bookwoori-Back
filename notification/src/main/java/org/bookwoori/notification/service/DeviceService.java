package org.bookwoori.notification.service;


import lombok.RequiredArgsConstructor;
import org.bookwoori.notification.domain.Device;
import org.bookwoori.notification.dto.request.RegisterRequestDto;
import org.bookwoori.notification.dto.response.DeviceResponseDto;
import org.bookwoori.notification.exception.CustomException;
import org.bookwoori.notification.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.bookwoori.notification.exception.CustomExceptionStatus.EMPTY_DEVICE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceResponseDto getDevice(Long userId) {
        Device device = deviceRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(EMPTY_DEVICE));
        return DeviceResponseDto.fromEntity(device);
    }

    @Transactional
    public void register(RegisterRequestDto request) {
        Optional<Device> device = deviceRepository.findByUserId(request.getUserId());

        if (device.isPresent()) {
            device.get().renew(request.getPlatform(), request.getToken());
            return;
        }

        Device newDevice = Device.register(request.getUserId(), request.getPlatform(), request.getToken());
        deviceRepository.save(newDevice);
    }


    @Transactional
    public void delete(Long userId) {
        Device device = deviceRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(EMPTY_DEVICE));
        device.delete();
    }
}
