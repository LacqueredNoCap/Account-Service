package account.handler;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String defaultMessage = ex
                .getBindingResult()
                .getAllErrors()
                .stream().findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("No message");

        ErrorMessage responseEntity = ErrorMessage
                .builder()
                .timestamp(new Date())
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(defaultMessage)
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build();

        return new ResponseEntity<>(responseEntity, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage responseEntity = ErrorMessage
                .builder()
                .timestamp(new Date())
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(ex.getLocalizedMessage())
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build();

        return new ResponseEntity<>(responseEntity, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage responseEntity = ErrorMessage
                .builder()
                .timestamp(new Date())
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(ex.getLocalizedMessage())
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build();

        return new ResponseEntity<>(responseEntity, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException handler,
//            HttpHeaders headers,
//            HttpStatus status, WebRequest request) {
//
//        //return new ResponseEntity<>(handler.getMessage(), HttpStatus.BAD_REQUEST);
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, handler.getMessage());
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException handler) {
//        //return new ResponseEntity<>(handler.getMessage(), HttpStatus.BAD_REQUEST);
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, handler.getMessage());
//    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public void handleConstraintViolationException(HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.BAD_REQUEST.value());
//    }
}
