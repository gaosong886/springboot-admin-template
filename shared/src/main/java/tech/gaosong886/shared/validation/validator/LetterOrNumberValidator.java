package tech.gaosong886.shared.validation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.gaosong886.shared.validation.annotation.LetterOrNumber;

/**
 * 自定义参数校验器：中英文字符或数字
 */
public class LetterOrNumberValidator implements ConstraintValidator<LetterOrNumber, String> {
    private static final Pattern ALLOWED_CHARACTERS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$");

    @Override
    public void initialize(LetterOrNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.equals(""))
            return true;

        Matcher matcher = ALLOWED_CHARACTERS_PATTERN.matcher(value);
        return matcher.matches();
    }
}
