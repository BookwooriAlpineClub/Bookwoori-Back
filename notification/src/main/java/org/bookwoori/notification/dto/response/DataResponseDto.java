package org.bookwoori.notification.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataResponseDto<T> extends CommonResponseDto {

    private T result;
}
