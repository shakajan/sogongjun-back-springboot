package multilearn.sogonjunspringserver.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {
    private String nickname;
    private String password;
}
