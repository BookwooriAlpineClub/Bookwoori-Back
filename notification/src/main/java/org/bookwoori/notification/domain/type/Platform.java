package org.bookwoori.notification.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    WEB("WEB"),
    IOS("IOS");

    private final String description;
}
