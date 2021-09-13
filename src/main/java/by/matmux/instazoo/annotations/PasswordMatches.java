package by.matmux.instazoo.annotations;

import by.matmux.instazoo.validations.PasswordMatchesValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidation.class)
@Documented
public @interface PasswordMatches {
    String message() default "Password do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
