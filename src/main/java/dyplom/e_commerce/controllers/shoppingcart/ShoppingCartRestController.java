package dyplom.e_commerce.controllers.shoppingcart;

import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entityService.CustomerService;
import dyplom.e_commerce.entityService.ShoppingCartService;
import dyplom.e_commerce.repositories.CustomerRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {

    private final ShoppingCartService shoppingCartService;
    private final CustomerRepository customerRepository;

    public ShoppingCartRestController(ShoppingCartService shoppingCartService, CustomerRepository customerRepository) {
        this.shoppingCartService = shoppingCartService;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/app/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable(name = "productId") Integer productId,
                                   @PathVariable("quantity") Integer quantity, HttpServletRequest request) {
        var emailBefore = request.getUserPrincipal();

        if (emailBefore == null) {
            return "You must be login";
        } else {
            Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, customerRepository.findByEmail(emailBefore.getName()));
            return updatedQuantity + "item(s) of this product were added to your cart";
        }

    }

    @PostMapping("/app/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable(name = "productId") Integer productId,
                                   @PathVariable("quantity") Integer quantity, HttpServletRequest request) {
        var email = request.getUserPrincipal();
        Customer customer = customerRepository.findByEmail(email.getName());
        float subtotal = shoppingCartService.updateQuantity(productId, quantity, customer);
        return String.valueOf(subtotal);
    }

    @DeleteMapping("/app/cart/remove/{productId}")
    public String removeProduct(@PathVariable(name = "productId") Integer productId, HttpServletRequest request) {
        var email = request.getUserPrincipal();
        Customer customer = customerRepository.findByEmail(email.getName());
        shoppingCartService.removeProduct(productId, customer);
        return "The product has been removed from shopping cart";
    }
}
