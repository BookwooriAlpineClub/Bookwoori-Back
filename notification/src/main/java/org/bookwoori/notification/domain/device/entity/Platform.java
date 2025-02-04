package org.bookwoori.notification.domain.device.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    WEB("WEB");

    private final String description;
}
