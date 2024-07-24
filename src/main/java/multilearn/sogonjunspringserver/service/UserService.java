package multilearn.sogonjunspringserver.service;

import lombok.AllArgsConstructor;
import multilearn.sogonjunspringserver.config.JwtUtil;
import multilearn.sogonjunspringserver.domain.User;
import multilearn.sogonjunspringserver.dto.user.LoginRequestDto;
import multilearn.sogonjunspringserver.dto.user.LoginResponseDto;
import multilearn.sogonjunspringserver.dto.user.RegisterRequestDto;
import multilearn.sogonjunspringserver.dto.user.RegisterResponseDto;
import multilearn.sogonjunspringserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;

    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        String hashedPassword = passwordEncoder.encode(registerRequestDto.getPassword());
        User user = new User();
        user.setPassword(hashedPassword);
        user.setGrade(registerRequestDto.getGrade());
        user.setNickname(registerRequestDto.getNickname());
        user.setNationality(registerRequestDto.getNationality());
        logger.info("[UserService] user: {}", user);
        User savedUser = userRepository.save(user);
        logger.info("[UserService] savedUser: {}", savedUser);
        return new RegisterResponseDto("User registered successfully.");
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws BadCredentialsException {
        var authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getNickname(), loginRequestDto.getPassword()
        );
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        var jwt = jwtUtil.createToken(auth);
        return new LoginResponseDto("Login successful.", "Bearer " + jwt);
    }

    //authentication test
    public String authTest(String nickname) {
        User user = userRepository.getByNickname(nickname);
        if (user == null) {
            return "not exist.";
        }
        return user.getNationality() + ", " + user.getGrade();
    }
}
