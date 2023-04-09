package tinysensormanager.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import tinysensormanager.dto.DbUserDTO;
import tinysensormanager.service.IDbUserService;

/**
 * Validator for validating {@link DbUserDTO} objects before they are saved in the database.
 *
 * @author manokel01
 * @version 1.0.0
 */
@Component
public class DbUserValidator implements Validator {

    private final IDbUserService userService;

    /**
     * Constructs a new DbUserValidator object with the given {@link IDbUserService} instance.
     *
     * @param userService the service used to perform database operations on User objects
     */
    @Autowired
    public DbUserValidator(IDbUserService userService) {
        this.userService = userService;
    }

    /**
     * Returns whether the validator can validate instances of the given class.
     *
     * @param clazz the class to check for support
     * @return whether objects of the given class can be validated
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return DbUserDTO.class.equals(clazz);
    }

    /**
     * Validates the given object and adds any errors found to the given Errors object.
     *
     * @param target the object to validate
     * @param errors the Errors object to add validation errors to
     */
    @Override
    public void validate(Object target, Errors errors) {
        DbUserDTO userToRegister = (DbUserDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty");
        if (userToRegister.getUsername().length() < 3 || userToRegister.getUsername().length() > 32) {
            errors.rejectValue("username", "size");
        }
        if (userService.usernameExists(userToRegister.getUsername())) {
            errors.rejectValue("email", "duplicate");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
        if (userToRegister.getPassword().length() < 3 || userToRegister.getPassword().length() > 32) {
            errors.rejectValue("password", "size");
        }

//        if (!Objects.equals(userToRegister.getPassword(), userToRegister.getConfirmPassword())) {
//            errors.rejectValue("confirmPassword", "confirmation");
//        }
    }
}

