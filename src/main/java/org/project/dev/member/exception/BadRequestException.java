package org.project.dev.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BadRequestException extends java.lang.RuntimeException {

    public BadRequestException(String message){
        super(message);
    }

}
