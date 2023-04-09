package tinysensormanager.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import tinysensormanager.dto.DeviceDTO;

/**
 * This class implements the Spring Validator interface for validating a {@link DeviceDTO} object.
 * It validates the fields of the {@link DeviceDTO} object and generates validation errors
 * using the Spring Errors object.
 *
 * @author manokel01
 * @version 1.0.0
 */
@Component
public class DeviceValidator implements Validator {

    /**
     * Returns true if the validator can validate instances of the given class.
     * This Validator can only validate {@link DeviceDTO} objects.
     *
     * @param clazz the class to check
     * @return true if this validator can validate the class, false otherwise
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return DeviceDTO.class == clazz;
    }

    /**
     * Validates the given object and generates validation errors using the given Errors object.
     * This method checks the validity of the model, mac, and ip fields of the {@link DeviceDTO} object.
     *
     * @param target the object to validate
     * @param errors the Spring Errors object to store any validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        DeviceDTO deviceDTO = (DeviceDTO) target;

        // Validate the model field
        ValidationUtils.rejectIfEmpty(errors, "model", "empty");
        if (deviceDTO.getModel().length() < 3 || deviceDTO.getModel().length() > 60) {
            errors.rejectValue("model", "size");
        }

        // Validate the mac field
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mac", "empty");
        if (deviceDTO.getMac().length() < 12 || deviceDTO.getMac().length() > 17) {
            errors.rejectValue("mac", "size");
        }

        // Validate the ip field
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ip", "empty");
        if (deviceDTO.getIp().length() < 7 || deviceDTO.getIp().length() > 39 ) {
            errors.rejectValue("ip", "size");
        }
    }
}

