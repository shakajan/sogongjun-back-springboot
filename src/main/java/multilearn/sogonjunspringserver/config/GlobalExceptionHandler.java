package multilearn.sogonjunspringserver.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import multilearn.sogonjunspringserver.dto.ExceptionDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ExceptionDto> handlerBadRequest(HttpClientErrorException.BadRequest e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto("이미지가 생성되기에 적절한 답변이 아닙니다."));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDto> handlerNullPointer(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ExceptionDto("이미지 또는 답변이 반환되지 않았습니다."));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionDto> handlerExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionDto("토큰이 만료되었습니다. 다시 로그인하시기 바랍니다."));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ExceptionDto> handlerMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionDto("로그인에 문제가 발생했습니다. 다시 로그인하시기 바랍니다."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDto> handlerBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionDto("아이디 또는 비밀번호가 잘못 입력되었습니다."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDto> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDto("이미 존재하는 닉네임입니다."));
    }
}
