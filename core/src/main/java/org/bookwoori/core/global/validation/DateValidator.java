package org.bookwoori.core.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;


public class DateValidator implements ConstraintValidator<ValidDateRange, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();

        if (obj instanceof ClimbingChannelCreateRequestDto dto) {
            // startDate가 오늘 이후인지 확인
            if (!dto.startDate().isAfter(today)) {
                throw new CustomException(ErrorCode.CLIMBING_INVALID_START_DATE);
            }
            // endDate가 오늘 이후인지 확인
            if (!dto.endDate().isAfter(today)) {
                throw new CustomException(ErrorCode.CLIMBING_INVALID_END_DATE);
            }
            // endDate가 startDate 이후인지 확인
            if (!dto.endDate().isAfter(dto.startDate())) {
                throw new CustomException(ErrorCode.CLIMBING_INVALID_INPUT_DATE);
            }
        } else if (obj instanceof ClimbingChannelUpdateRequestDto dto) {
            // startDate가 오늘 이후인지 확인
            if (!dto.startDate().isAfter(today)) {
                throw new CustomException(ErrorCode.CLIMBING_INVALID_START_DATE);
            }
            // endDate가 오늘 이후인지 확인
            if (!dto.endDate().isAfter(today)) {
                throw new CustomException(ErrorCode.CLIMBING_INVALID_END_DATE);
            }
            // endDate가 startDate 이후인지 확인
            if (!dto.endDate().isAfter(dto.startDate())) {
                throw new CustomException(ErrorCode.CLIMBING_INVALID_INPUT_DATE);
            }
        } else if (obj instanceof RecordRequestDto dto) {
            // startDate가 오늘 이전인지 확인
            if (dto.startDate() != null && !dto.startDate().isBefore(today) && !dto.startDate().isEqual(today)) {
                throw new CustomException(ErrorCode.RECORD_INVALID_START_DATE);
            }
            // endDate가 오늘 이전인지 확인
            if (dto.endDate() != null && !dto.endDate().isBefore(today) && !dto.endDate().isEqual(today)) {
                throw new CustomException(ErrorCode.RECORD_INVALID_END_DATE);
            }
            // startDate가 endDate보다 이전인지 확인 (같은 날짜도 가능)
            if (dto.startDate() != null && dto.endDate() != null && dto.startDate().isAfter(dto.endDate())) {
                throw new CustomException(ErrorCode.RECORD_INVALID_INPUT_DATE);
            }
        }
        return true;
    }
}
