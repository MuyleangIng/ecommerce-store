package co.cstad.sen.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityException {

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(AuthenticationException.class)
//    public BaseError<?> handleServiceException(AuthenticationException e) {
//        return BaseError.builder()
//                .status(false)
//                .code(HttpStatus.UNAUTHORIZED.value())
//                .timestamp(LocalDateTime.now())
//                .message("Something went wrong..!, please check.")
//                .errors(e.getMessage())
//                .build();
//    }
}
