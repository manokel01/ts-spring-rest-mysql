package tinysensormanager.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import tinysensormanager.dto.UserDTO;

/**
 * This class implements the Validator interface to validate UserDTO objects.
 * It checks that the fields firstname, lastname, and email are not empty, and that they are within a certain length range.
 *
 * @author manokel01
 * @version 1.0.0
 */
@Component
public class UserValidator implements Validator {

    /**
     * Returns whether the Validator can validate instances of the given class.
     * @param clazz the Class that this Validator is being asked if it can validate
     * @return true if this Validator can indeed validate objects of the given class; false otherwise
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class == clazz;
    }

    /**
     * Validates the given object and its Errors instance.
     * @param target the object that is to be validated
     * @param errors contextual state about the validation process (may be {@literal null})
     */
    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        // Check if firstname is empty or too long/short
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "empty");
        if (userDTO.getFirstname().length() < 3 || userDTO.getFirstname().length() > 60) {
            errors.rejectValue("firstname", "size");
        }

        // Check if lastname is empty or too long/short
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "empty");
        if (userDTO.getLastname().length() < 3 || userDTO.getLastname().length() > 50) {
            errors.rejectValue("lastname", "size");
        }

        // Check if email is empty or too long/short
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty");
        if (userDTO.getEmail().length() < 6 || userDTO.getEmail().length() > 256 ) {
            errors.rejectValue("lastname", "size");
        }
    }
}

