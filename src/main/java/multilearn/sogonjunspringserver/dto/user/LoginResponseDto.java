package multilearn.sogonjunspringserver.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String message;
    private String token;
    private String nickname;
    private String nationality;
    private Integer grade;
    private Long id;
}