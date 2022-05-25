package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.User;
import dyplom.e_commerce.entityService.UserService;
import dyplom.e_commerce.security.AppUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin-page")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal AppUserDetails loggedUser,
                              Model model) {
        String email = loggedUser.getUsername();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "admin_user/account_form";
    }

    @PostMapping("/account/update")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal AppUserDetails loggedUser){
        User savedUser = userService.updateAccount(user);
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "Your account details has been updated");
        return "redirect:/admin-page/account";
    }
}
