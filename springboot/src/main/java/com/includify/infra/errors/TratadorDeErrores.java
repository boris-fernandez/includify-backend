package com.includify.infra.errors;

import com.includify.domain.ValidacionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErrores {

//    @ExceptionHandler(ValidacionException.class)
//    public ResponseEntity<?> tratarErrorDeValidacion(ValidacionException e){
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ValidacionExecptionDTO(e.getMessage()));
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<?> tratarError400(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<?> tratarError405(HttpRequestMethodNotSupportedException e){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ValidacionExecptionDTO(e.getMessage()));
    }

//    MaxUploadSizeExceededException

//    @ExceptionHandler(RuntimeException.class)
//    public final ResponseEntity<?> tratarErrorRuntimeException(RuntimeException e){
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ValidacionExecptionDTO(e.getMessage()));
//    }
}
