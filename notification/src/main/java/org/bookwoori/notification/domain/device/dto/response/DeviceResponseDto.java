package org.bookwoori.notification.domain.device.dto.response;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import org.bookwoori.notification.domain.device.entity.Device;
import org.bookwoori.notification.domain.device.entity.Platform;


@Builder
public record DeviceResponseDto(
        Long memberId,

        @Enumerated(EnumType.STRING)
        Platform platform,

        String token,

        boolean status
) {

    public static DeviceResponseDto from(Device device) {

        return DeviceResponseDto.builder()
                .memberId(device.getMemberId())
                .platform(device.getPlatform())
                .token(device.getToken())
                .status(device.isStatus())
                .build();

    }
}
