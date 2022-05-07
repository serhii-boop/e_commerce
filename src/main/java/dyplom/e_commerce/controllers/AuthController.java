package dyplom.e_commerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

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

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }
}
