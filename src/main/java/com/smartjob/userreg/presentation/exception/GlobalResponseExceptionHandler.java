package com.smartjob.userreg.presentation.exception;

import com.smartjob.userreg.application.UserNotFoundException;
import com.smartjob.userreg.presentation.ErrorMessage;
import java.security.InvalidParameterException;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalResponseExceptionHandler {

    /**
     * Handles exceptions of type MethodArgumentNotValidException, which is thrown
     * when a method argument annotated with validation annotations (such as @Valid) fails validation.
     *
     * @param e the MethodArgumentNotValidException exception
     * @return a ResponseEntity with a 400 Bad Request status and an error message from the exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleExceptionsForInvalidPayloadFields(MethodArgumentNotValidException e) {
        var errors = e.getBindingResult().getAllErrors();
        if (errors.size() == NumberUtils.INTEGER_ONE.intValue()) {
            return ResponseEntity.badRequest().body(ErrorMessage.instance(errors.stream().findFirst().get().getDefaultMessage()));
        }
        var errorMgs = errors.stream()
                .map(err -> ErrorMessage.instance(err.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorMgs);
    }

    /**
     * Handles exceptions of type InvalidParameterException, which is thrown when an invalid parameter is encountered in the request.
     *
     * @param e the InvalidParameterException exception
     * @return a ResponseEntity with a 400 Bad Request status and the error message from the exception
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameterException(InvalidParameterException e) {
        return ResponseEntity.badRequest().body(ErrorMessage.instance(e.getMessage()));
    }

    /**
     * Handles exceptions of type UserNotFoundException, which is thrown when a user is not found in the db.
     *
     * @param e the UserNotFoundException exception
     * @return a ResponseEntity with a 404 Not Found status and the error message from the exception
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.instance(e.getMessage()));
    }

}
