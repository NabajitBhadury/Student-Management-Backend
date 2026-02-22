package org.perfecto.springlearning.exceptions.genericExceptions;

// this is a custom exception class that will be used to handle the case when a resource is not found in the database and we will throw this exception from the service layer when we try to find a student by id and the student is not found in the database and then we will handle this exception in the global exception handler and return a response with a message and a status code of 404 not found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
