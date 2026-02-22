// This is the error response class which is used to send structured error messages to the client for any exceptions

package org.perfecto.springlearning.exceptions;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String error;
    private String path;
    private Integer status;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;
}
