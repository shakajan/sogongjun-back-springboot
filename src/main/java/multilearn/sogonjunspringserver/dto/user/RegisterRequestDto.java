package multilearn.sogonjunspringserver.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestDto {
    private String nickname;
    private String nationality;
    private Integer grade;
    private String password;
}
