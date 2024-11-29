package org.bookwoori.notification.domain.dto.response;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import org.bookwoori.notification.domain.Device;
import org.bookwoori.notification.domain.type.Platform;


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
