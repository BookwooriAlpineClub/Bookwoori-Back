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
public class DeviceTokenResponse {
    @Enumerated(EnumType.STRING)
    private Platform platform;
    private String token;

    public static DeviceTokenResponse fromEntity(Device device) {
        DeviceTokenResponse response = new DeviceTokenResponse();
        response.setPlatform(device.getPlatform());
        response.setToken(device.getToken());
        return response;
    }

    @Override
    public String toString() {
        return "{" +
                "platform=" + platform +
                ", token='" + token + '\'' +
                '}';
    }
}
