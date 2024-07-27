package multilearn.sogonjunspringserver.controller;

import lombok.AllArgsConstructor;
import multilearn.sogonjunspringserver.dto.SimpleMessageDto;
import multilearn.sogonjunspringserver.dto.user.*;
import multilearn.sogonjunspringserver.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @CrossOrigin(origins = "*")
    @PostMapping("/api/users/login")
    @ResponseBody
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/api/users/register")
    public RegisterResponseDto register(@RequestBody RegisterRequestDto registerRequestDto) {
        return userService.register(registerRequestDto);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/api/users/{nickname}")
    public SimpleMessageDto deleteUser(@PathVariable String nickname) {
        return userService.deleteUser(nickname);
    }

    @GetMapping("/api/users/test/{nickname}")
    public String authTest(@PathVariable("nickname") String nickname) {
        return userService.authTest(nickname);
    }
}
