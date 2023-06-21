package peaksoft.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;
@Builder
public record MyExceptionResponse(
        HttpStatus httpStatus,
        String exceptionClassName,
        String message
) {
}
