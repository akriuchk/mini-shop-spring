package org.akriuchk.minishop.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
public class FileExtensionValidator implements ConstraintValidator<ValidExtension, String> {
    private List<String> validExtensions;

    public void initialize(ValidExtension constraintAnnotation) {
        this.validExtensions = Arrays.asList(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(String originalFilename, ConstraintValidatorContext constraintValidatorContext) {
        int i = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(i).toLowerCase(Locale.ROOT);

        if (!validExtensions.contains(extension)) {
            log.error("Extension of {} is not permitted: {}", extension, validExtensions );
            return false;
        }
        return true;
    }
}
