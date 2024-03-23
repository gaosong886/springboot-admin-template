package tech.gaosong886.shared.validation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.gaosong886.shared.validation.annotation.FilePath;

/**
 * 自定义参数校验器：文件路径
 */
public class FilePathValidator implements ConstraintValidator<FilePath, String> {
    private static final Pattern ALLOWED_CHARACTERS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\/\\-_]+$");

    @Override
    public void initialize(FilePath constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.equals(""))
            return true;

        Matcher matcher = ALLOWED_CHARACTERS_PATTERN.matcher(value);
        return matcher.matches();
    }
}
