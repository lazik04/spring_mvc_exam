package uz.pdp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uz.pdp.dto.UserSignUpDto;
import uz.pdp.service.UserService;


@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/auth/logout")
    public String logout() {
        return "logout";
    }


    @GetMapping("/auth/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("auth/signup")
    public String registerUser(@ModelAttribute UserSignUpDto userSignUpDto) {
        userService.saveUser(userSignUpDto);
        return "login";
    }

}
