package org.akriuchk.minishop.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileExtensionValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExtension {
    String message() default "Invalid extension!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}