package org.bookwoori.core.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum Grade {

    Baekdusan(10, "백두산", 2744),
    Hallasan(9, "한라산", 1950),
    Jirisan(8, "지리산", 1915),
    Seoraksan(7, "설악산", 1708),
    Geumgangsan(6, "금강산", 1638),
    Sobaeksan(5, "소백산", 1439),
    Mudeungsan(4, "무등산", 997),
    Bukhansan(3, "북한산", 727),
    Achasan(2, "아차산", 296),
    Dongsan(1, "동산", 100);

    private final int level;
    private final String mountain;
    private final int height;

    @JsonCreator
    public static Grade from(String s) {
        try {
            return Grade.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
