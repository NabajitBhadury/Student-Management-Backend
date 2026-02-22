// This class is a global exception handler for a Spring Boot application. It uses the @RestControllerAdvice annotation to handle exceptions across the whole application. The class defines several methods to handle specific exceptions, such as ResourceNotFoundException, DataIntegrityViolationException, and MethodArgumentNotValidException. Each method builds a standardized error response using the buildResponse method, which creates an ErrorResponse object containing details about the error, including a message, status, path, timestamp, and any validation errors if applicable. The handleGlobal method serves as a catch-all for any unhandled exceptions, returning a generic internal server error response.
// the methods will not be directly called in the application but it will be called internally when an exception occurs as the @ExceptionHandler annotation is being used there

package org.perfecto.springlearning.exceptions;

import org.perfecto.springlearning.exceptions.genericExceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // this annotation is used to handle exceptions across the whole application and it is a specialization of the @ControllerAdvice annotation which is used to handle exceptions across the whole application and it is used to handle exceptions in RESTful web services and it is a combination of @ControllerAdvice and @ResponseBody annotations
public class GlobalExceptionHandler {
    // this method is used to build the error response for all the exceptions and it takes the message, status, path and timestamp as parameters and it returns a response entity with the error response
    private ResponseEntity<ErrorResponse> buildResponse(String message ,HttpStatus status, WebRequest webRequest, Map<String, String> validationErrors) {
        ErrorResponse response = new ErrorResponse(message, status.getReasonPhrase(),webRequest.getDescription(false).replace("uri=", ""), status.value(), LocalDateTime.now(), validationErrors);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class) // this annotation is used to handle the specific exception and it takes the exception class as a parameter
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request){
        String message = "Resource not found";
        return buildResponse(message, HttpStatus.NOT_FOUND, request,null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex, WebRequest request) {
        String message = "Database conflict: Likely a duplicate roll number or missing required field.";
        return buildResponse(message, HttpStatus.CONFLICT, request,null);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return buildResponse("Validation failed", HttpStatus.BAD_REQUEST, request,errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex, WebRequest request) {
        return buildResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }
}
