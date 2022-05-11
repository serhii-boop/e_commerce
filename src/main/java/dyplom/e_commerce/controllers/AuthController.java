package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.Role;
import dyplom.e_commerce.entities.User;
import dyplom.e_commerce.entityService.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/app")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


//    @GetMapping("")
//    public String viewHomePage() {
//        return "index";
//    }
//
//    @GetMapping("/register")
//    public String showSignUpForm(Model model) {
//        model.addAttribute("user", new User());
//        return "signup_form";
//    }

//    @PostMapping("/process-register")
//    public String processRegistration(User user) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
//        user.setRegistrationDate(LocalDateTime.now());
//        user.setRoleId(1);
//        userService.saveUser(user);
//        return "register_success";
//    }

//    @GetMapping("/order-list")
//    public String viewOrderList() {
//        return "orders";
//    }
//
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process-register")
    public String processRegistration(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRegistrationDate(LocalDateTime.now());
        user.setRole(Role.USER);
        userService.saveUser(user);
        return "register_success";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }
}
