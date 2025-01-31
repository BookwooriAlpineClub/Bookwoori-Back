package org.bookwoori.core.domain.exp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bookwoori.core.domain.exp.entity.ExpType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GrantExpContainer.class)
public @interface GrantExp {

  ExpType type();

  int baseXp() default 0;

  String additionalInfo() default "";
}