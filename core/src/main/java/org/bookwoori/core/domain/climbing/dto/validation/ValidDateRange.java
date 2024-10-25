package org.bookwoori.core.domain.climbing.dto.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ TYPE }) // 클래스 레벨에서 유효성 검사를 수행
@Retention(RUNTIME)
public @interface ValidDateRange {
    String message() default "INVALID_DATE_RANGE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
