package tinysensormanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * A controller advice that handles exceptions and errors that may occur during requests.
 *
 * @author manokel01
 * @version 1.0.0
 */
@ControllerAdvice
public class ErrorController {

    /**
     * Handles requests to the "/error" endpoint by returning the "error" view.
     *
     * @return the name of the "error" view
     */
    @GetMapping("/error")
    public String handleError() {
        return "error";
    }

    /**
     * Handles exceptions of type {@link IllegalArgumentException} and {@link IllegalStateException}
     * by returning an HTTP bad request status code along with the exception message.
     *
     * @param ex the exception that occurred
     * @return a {@link ResponseEntity} with a bad request status code and the exception message
     */
    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleBadRequest(RuntimeException ex) {
        String message = ex.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions by returning an HTTP internal server error status code
     * along with a message that describes the error that occurred.
     *
     * @param e the exception that occurred
     * @return a {@link ResponseEntity} with an internal server error status code and a message
     *         that describes the error that occurred
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }
}



