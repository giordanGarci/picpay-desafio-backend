package com.picpaysimplificado.picpaysimplificado.infra;

import com.picpaysimplificado.picpaysimplificado.dtos.ExceptionDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity threatDuplicateEntry(DataIntegrityViolationException exception) {
        ExceptionDto exceptionDto = new ExceptionDto("User already exist ", "400");
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity threat404(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGeneralExceptions(Exception exception){
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), "500");
        return ResponseEntity.internalServerError().body(exceptionDto);
    }
}
