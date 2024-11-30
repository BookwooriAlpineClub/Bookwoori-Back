package org.bookwoori.core.domain.climbing.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;

public class DateValidator implements ConstraintValidator<ValidDateRange, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();
        LocalDate minDate = today.plusDays(1); // 최소 기준 날짜 (당일 + 1)

        if (obj instanceof ClimbingChannelCreateRequestDto dto) {
            // startDate가 최소 기준 이후인지 확인
            if (dto.startDate().isBefore(minDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "INVALID_INPUT_DATE-시작 날짜는 최소 현재 날짜의 다음 날 이후여야 합니다.")
                    .addConstraintViolation();
                return false;
            }
            // endDate가 최소 기준 이후인지 확인
            if (dto.endDate().isBefore(minDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "INVALID_INPUT_DATE-종료 날짜는 최소 현재 날짜의 다음 날 이후여야 합니다.")
                    .addConstraintViolation();
                return false;
            }
            // endDate가 startDate 이후인지 확인
            if (dto.endDate().isBefore(dto.startDate())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "INVALID_INPUT_DATE-종료 날짜는 시작 날짜 이후여야 합니다.")
                    .addConstraintViolation();
                return false;
            }
        } else if (obj instanceof ClimbingChannelUpdateRequestDto dto) {
            // startDate가 최소 기준 이후인지 확인
            if (dto.startDate().isBefore(minDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "INVALID_INPUT_DATE-시작 날짜는 최소 현재 날짜의 다음 날 이후여야 합니다.")
                    .addConstraintViolation();
                return false;
            }
            // endDate가 최소 기준 이후인지 확인
            if (dto.endDate().isBefore(minDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "INVALID_INPUT_DATE-종료 날짜는 최소 현재 날짜의 다음 날 이후여야 합니다.")
                    .addConstraintViolation();
                return false;
            }
            // endDate가 startDate 이후인지 확인
            if (dto.endDate().isBefore(dto.startDate())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "INVALID_INPUT_DATE-종료 날짜는 시작 날짜 이후여야 합니다.")
                    .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
