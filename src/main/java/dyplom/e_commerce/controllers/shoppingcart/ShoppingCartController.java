package dyplom.e_commerce.controllers.shoppingcart;

import dyplom.e_commerce.entities.CartItem;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entityService.ShoppingCartService;
import dyplom.e_commerce.repositories.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final CustomerRepository customerRepository;

    public ShoppingCartController(ShoppingCartService shoppingCartService, CustomerRepository customerRepository) {
        this.shoppingCartService = shoppingCartService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/app/cart")
    public String viewCart(Model model, HttpServletRequest request) {
        var email = request.getUserPrincipal().getName();
        Customer customer = customerRepository.findByEmail(email);
        var cartItem = shoppingCartService.listCartItems(customer);
        float sum = cartItem.stream().map(CartItem::getSubTotal).reduce(0F, Float::sum);
        model.addAttribute("cartItems", cartItem);
        model.addAttribute("totalSum", sum);
        return "cart/shopping_cart";
    }
}
