package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entityService.ShopCartService;
import dyplom.e_commerce.entityService.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class ShopCartController {

    private final ShopCartService shopCartService;
    private final UserService userService;

    public ShopCartController(ShopCartService shopCartService, UserService userService) {
        this.shopCartService = shopCartService;
        this.userService = userService;
    }

    @GetMapping("/cart")
    public String showShoppingCart(Model model,
                                   @AuthenticationPrincipal Authentication authentication) {

        return "shoppingCart";
    }
}
