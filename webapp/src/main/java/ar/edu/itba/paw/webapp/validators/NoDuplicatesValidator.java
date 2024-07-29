package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.NoDuplicates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoDuplicatesValidator implements ConstraintValidator<NoDuplicates, List<?>> {

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        if (list == null) {
            return true; // Other validators will handle this
        }

        Set<Object> set = new HashSet<>();
        for (Object obj : list) {
            if (!set.add(obj)) {
                return false; // Duplicate element found
            }
        }

        return true;
    }
}
