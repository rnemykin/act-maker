package ru.act.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.act.common.ValidationException

@ControllerAdvice
class ExceptionHandlerController {

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<String> handleValidationException(ValidationException ex) {
        HttpHeaders header = new HttpHeaders();
        header.set("Act-Error-Message", URLEncoder.encode(ex.message, "UTF-8"))
        new ResponseEntity<String>("", header, HttpStatus.UNPROCESSABLE_ENTITY)
    }

}
