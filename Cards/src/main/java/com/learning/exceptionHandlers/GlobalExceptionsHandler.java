package com.learning.exceptionHandlers;

import com.learning.dto.ErrorResponseDto;
import com.learning.exception.CardNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionsHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(exception = CardNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleCardNotFoundException(WebRequest request,
                                                                   CardNotFoundException exception) {
    return ResponseEntity.badRequest().body(new ErrorResponseDto(HttpStatus.NOT_FOUND,exception.getMessage(),
        request.getDescription(false)));
  }

  @ExceptionHandler(exception = Exception.class)
  public ResponseEntity<ErrorResponseDto> handleCardException(WebRequest request, Exception exception) {
    return ResponseEntity.internalServerError().body(new ErrorResponseDto(HttpStatus.BAD_REQUEST,exception.getMessage(),
        request.getDescription(false)));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    List<String> errors = new ArrayList<>();
    BindingResult bindingResult = ex.getBindingResult();
    bindingResult.getFieldErrors().forEach((fieldError) -> {
      errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
    });
    return ResponseEntity.badRequest().body(new ErrorResponseDto(HttpStatus.BAD_REQUEST,
        "Invalid Arguments : "+ errors,
        request.getDescription(false)));
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    return ResponseEntity.badRequest().body(
        new ErrorResponseDto(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request.getDescription(false)
        )
    );
  }

}
