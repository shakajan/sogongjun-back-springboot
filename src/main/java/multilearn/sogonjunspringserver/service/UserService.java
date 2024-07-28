package multilearn.sogonjunspringserver.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import multilearn.sogonjunspringserver.config.JwtUtil;
import multilearn.sogonjunspringserver.domain.Answer;
import multilearn.sogonjunspringserver.domain.Question;
import multilearn.sogonjunspringserver.domain.User;
import multilearn.sogonjunspringserver.dto.SimpleMessageDto;
import multilearn.sogonjunspringserver.dto.user.*;
import multilearn.sogonjunspringserver.repository.AnswerRepository;
import multilearn.sogonjunspringserver.repository.NFTRepository;
import multilearn.sogonjunspringserver.repository.QuestionRepository;
import multilearn.sogonjunspringserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final NFTRepository nftRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;

    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) throws DataIntegrityViolationException {
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
        User user = userRepository.getByNickname(loginRequestDto.getNickname());
        if(user == null) {
            throw new EntityNotFoundException("User not found.");
        }
        return new LoginResponseDto(
                "Login successful.",
                "Bearer " + jwt,
                user.getNickname(),
                user.getNationality(),
                user.getGrade(),
                user.getId()
                );
    }

    public SimpleMessageDto deleteUser(String nickname) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if(user == null) {
            throw new BadCredentialsException("User not found.");
        }
        logger.info("[UserService] nickname in token: {}", user.getNickname());
        logger.info("[UserService] nickname in path : {}", nickname);
        if(user.getNickname().equals(nickname)) {
            List<Question> questions = questionRepository.findAllByUserId(user.getId());
            for(Question question : questions) {
                logger.info("[UserService] question: {}", question);
                Answer answer = answerRepository.findByQuestionId(question.getId());
                nftRepository.deleteByAnswerId(answer.getId());
                answerRepository.deleteById(answer.getId());
                questionRepository.deleteById(question.getId());
            }
            userRepository.delete(user);
        } else {
            throw new BadCredentialsException("Wrong nickname.");
        }
        return new SimpleMessageDto("user delete successfully.");
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
