package tech.gaosong886.shared.validation.validator;

import java.lang.reflect.Field;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.gaosong886.shared.validation.annotation.EnumValue;

/**
 * 自定义参数校验器：枚举值
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Integer> {

    private Class<? extends Enum<?>> enumClass;
    private Field valueField;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();

        // 约定被校验的枚举都有一个 value 字段，使用反射获取这个字段
        Field[] fields = this.enumClass.getDeclaredFields();
        for (Field field : fields) {
            if ("value".equals(field.getName()) && field.getType().equals(Integer.class)) {
                field.setAccessible(true);
                this.valueField = field;
                break;
            }
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            try {
                if ((Integer) this.valueField.get(enumConstant) == value)
                    return true;
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
