package org.bookwoori.core.domain.climbing.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;


public class DateValidator implements ConstraintValidator<ValidDateRange, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();
        LocalDate minDate = today.plusDays(1); // 최소 기준 날짜 (오늘 + 1)

        if (obj instanceof ClimbingChannelCreateRequestDto dto) {
            // startDate가 오늘 이후인지 확인
            if (!dto.startDate().isAfter(today)) {
                throw new CustomException(ErrorCode.INVALID_START_DATE);
            }
            // endDate가 오늘 이후인지 확인
            if (!dto.endDate().isAfter(today)) {
                throw new CustomException(ErrorCode.INVALID_END_DATE);
            }
            // endDate가 startDate 이후인지 확인
            if (!dto.endDate().isAfter(dto.startDate())) {
                throw new CustomException(ErrorCode.INVALID_INPUT_DATE);
            }
        } else if (obj instanceof ClimbingChannelUpdateRequestDto dto) {
            // startDate가 오늘 이후인지 확인
            if (!dto.startDate().isAfter(today)) {
                throw new CustomException(ErrorCode.INVALID_START_DATE);
            }
            // endDate가 오늘 이후인지 확인
            if (!dto.endDate().isAfter(today)) {
                throw new CustomException(ErrorCode.INVALID_END_DATE);
            }
            // endDate가 startDate 이후인지 확인
            if (!dto.endDate().isAfter(dto.startDate())) {
                throw new CustomException(ErrorCode.INVALID_INPUT_DATE);
            }
        }
        return true;
    }
}
