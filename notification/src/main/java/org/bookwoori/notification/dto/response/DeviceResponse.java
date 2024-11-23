package org.bookwoori.notification.dto.response;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bookwoori.notification.domain.Device;
import org.bookwoori.notification.domain.type.Platform;


@Getter
@Setter
@NoArgsConstructor
public class DeviceResponse {

    private Long userId;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    private String token;

    private boolean status;

    public static DeviceResponse fromEntity(Device device) {
        DeviceResponse response = new DeviceResponse();
        response.setUserId(device.getUserId());
        response.setPlatform(device.getPlatform());
        response.setToken(device.getToken());
        response.setStatus(device.isStatus());
        return response;
    }
}
