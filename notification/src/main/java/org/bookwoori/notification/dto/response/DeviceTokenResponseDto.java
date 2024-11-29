package org.bookwoori.notification.dto.response;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import org.bookwoori.notification.domain.Device;
import org.bookwoori.notification.domain.type.Platform;


@Builder
public record DeviceTokenResponseDto(
        @Enumerated(EnumType.STRING)
        Platform platform,
        String token
) {


    public static DeviceTokenResponseDto from(Device device) {

        return DeviceTokenResponseDto.builder()
                .platform(device.getPlatform())
                .token(device.getToken())
                .build();
    }

    @Override
    public String toString() {
        return "{" +
                "platform=" + platform +
                ", token='" + token + '\'' +
                '}';
    }
}
