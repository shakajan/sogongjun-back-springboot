package multilearn.sogonjunspringserver.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import multilearn.sogonjunspringserver.dto.SimpleMessageDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<SimpleMessageDto> handlerBadRequest(HttpClientErrorException.BadRequest e) {
        String methodName = getMethodName(e);
        if (methodName.equals("getImage")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleMessageDto("이미지가 생성되기에 적절한 답변이 아닙니다."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleMessageDto(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<SimpleMessageDto> handlerNotFound(EntityNotFoundException e) {
        String methodName = getMethodName(e);
        if (methodName.equals("deleteUser")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleMessageDto("존재하지 않는 유저입니다."));
        }
        if (methodName.equals("getImage")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleMessageDto("존재하지 않는 질문입니다."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleMessageDto(e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<SimpleMessageDto> handlerNullPointer(NullPointerException e) {
        String methodName = getMethodName(e);
        if(methodName.equals("deleteUser")) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new SimpleMessageDto("존재하지 않는 유저입니다."));
        }
        if(methodName.equals("getImage")) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new SimpleMessageDto("이미지 또는 답변이 반환되지 않았습니다."));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleMessageDto(e.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<SimpleMessageDto> handlerExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SimpleMessageDto("토큰이 만료되었습니다. 다시 로그인하시기 바랍니다."));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<SimpleMessageDto> handlerMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SimpleMessageDto("로그인에 문제가 발생했습니다. 다시 로그인하시기 바랍니다."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<SimpleMessageDto> handlerBadCredentialsException(BadCredentialsException e) {
        String methodName = getMethodName(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SimpleMessageDto("아이디 또는 비밀번호가 잘못 입력되었습니다."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<SimpleMessageDto> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
        String methodName = getMethodName(e);
        return switch (methodName) {
            case "createUser" ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleMessageDto("이미 존재하는 닉네임입니다."));
            case "getAnswer" ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleMessageDto("질문을 저장할 수 없습니다."));
            case "getImage" ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleMessageDto("답변을 저장할 수 없습니다."));
            default ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleMessageDto(e.getMessage()));
        };
    }

    private String getMethodName(Exception e) {
        StackTraceElement element = e.getStackTrace()[0];
        log.error("[ExceptionHandler]:" + element.getClassName() + "." + element.getMethodName(), e);
        return element.getMethodName();
    }
}
