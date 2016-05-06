package ru.act.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.act.common.ValidationException

@ControllerAdvice
class ExceptionHandlerController {

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<String> handleValidationException(ValidationException ex) {
        new ResponseEntity<String>(ex.message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

}
