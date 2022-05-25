package dyplom.e_commerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin-page")
public class MainController {

    @GetMapping("")
    public String viewHomePage() {
        return "indexForAdmin";
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }
}
