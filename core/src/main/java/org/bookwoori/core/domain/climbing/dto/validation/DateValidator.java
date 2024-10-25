package org.bookwoori.core.domain.climbing.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;

import java.time.LocalDate;


public class DateValidator implements ConstraintValidator<ValidDateRange, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();

        if (obj instanceof ClimbingChannelCreateRequestDto dto) {
            // Create Request DTO: startDate와 endDate가 오늘 이후인지 확인
            if (dto.startDate().isBefore(today) || dto.endDate().isBefore(today)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("INVALID_INPUT_DATE-날짜는 현재 날짜 이후여야 합니다.")
                        .addConstraintViolation();
                return false;
            }
            // endDate가 startDate 이후인지 확인
            if (dto.endDate().isBefore(dto.startDate())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("INVALID_INPUT_DATE-종료 날짜는 시작 날짜 이후여야 합니다.")
                        .addConstraintViolation();
                return false;
            }
        } else if (obj instanceof ClimbingChannelUpdateRequestDto dto) {
            // Update Request DTO: endDate가 현재 날짜 이후인지 확인
            if (dto.endDate().isBefore(today)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("INVALID_INPUT_DATE-종료 날짜는 현재 날짜 이후여야 합니다.")
                        .addConstraintViolation();
                return false;
            }
            // endDate가 startDate 이후인지 확인
            if (dto.endDate().isBefore(dto.startDate())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("INVALID_INPUT_DATE-종료 날짜는 시작 날짜 이후여야 합니다.")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
