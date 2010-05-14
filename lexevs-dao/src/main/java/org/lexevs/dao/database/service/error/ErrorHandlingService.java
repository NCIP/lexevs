package org.lexevs.dao.database.service.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorHandlingService {

	boolean matchAllMethods() default false;

	Class<?>[] matchAnnotatedMethods() default {};
}
