package org.bookwoori.core.domain.xp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GrantXpContainer.class)
public @interface GrantXp {

  XpType type();

  int baseXp() default 0;

  String additionalInfo() default "";
}