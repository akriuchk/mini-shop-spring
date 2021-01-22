package org.akriuchk.minishop.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;

public class FileExtensionValidator implements ConstraintValidator<ValidExtension, String> {
    public static final String EXCEL_TABLE = "xlsx";


    @Override
    public boolean isValid(String originalFilename, ConstraintValidatorContext constraintValidatorContext) {
        int i = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(i).toLowerCase(Locale.ROOT);
        return extension.equalsIgnoreCase(EXCEL_TABLE);
    }
}
