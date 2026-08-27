package com.manuel.zaguan_inmobiliarias.exception;

import com.manuel.zaguan_inmobiliarias.dto.response.error.ApiErrorResponse;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.InvalidPhotoException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.PhotoLimitExceededException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.PropertyPhotoNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Atrapa las excepciones de todos los controllers y las devuelve siempre con el mismo formato
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePropertyNotFound(PropertyNotFoundException e, HttpServletRequest request){
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(PropertyPhotoNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePhotoNotFound(PropertyPhotoNotFoundException e, HttpServletRequest request){
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(InvalidPhotoException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPhoto(InvalidPhotoException e, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(PhotoLimitExceededException.class)
    public ResponseEntity<ApiErrorResponse> handlePhotoLimit(PhotoLimitExceededException e, HttpServletRequest request){
        return build(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUploadSize(MaxUploadSizeExceededException e, HttpServletRequest request){
        return build(HttpStatus.CONTENT_TOO_LARGE, "The file is too large", request);
    }

    //Los @Valid que fallan: junta los mensajes campo por campo en uno solo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request){
        List<String> messages = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()){
            messages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }

        return build(HttpStatus.BAD_REQUEST, String.join(", ", messages), request);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, HttpServletRequest request){
        ApiErrorResponse body = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI());

        return ResponseEntity.status(status).body(body);
    }
}
