package tech.gaosong886.shared.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import tech.gaosong886.shared.validation.validator.LetterOrNumberValidator;

/**
 * 自定义注解：参数校验 - 中英文字符或数字
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LetterOrNumberValidator.class)
public @interface LetterOrNumber {
    String message() default "Property must contain only numbers, Chinese or English letters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}