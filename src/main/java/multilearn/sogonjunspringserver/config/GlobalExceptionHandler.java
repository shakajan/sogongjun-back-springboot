package multilearn.sogonjunspringserver.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<String> handlerBadRequest(HttpClientErrorException.BadRequest e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지가 생성되기에 적절한 답변이 아닙니다.");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handlerNullPointer(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("이미지 또는 답변이 반환되지 않았습니다.");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handlerExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다. 다시 로그인하시기 바랍니다.");
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handlerMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 문제가 발생했습니다. 다시 로그인하시기 바랍니다.");
    }
}
