package tech.gaosong886.shared.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import tech.gaosong886.shared.validation.validator.FilePathValidator;

/**
 * 自定义注解：参数校验 - 文件路径
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilePathValidator.class)
public @interface FilePath {
    String message() default "Property is not a valid file path.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}