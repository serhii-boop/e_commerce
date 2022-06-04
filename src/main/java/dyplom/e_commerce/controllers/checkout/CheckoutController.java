package dyplom.e_commerce.controllers.checkout;

import dyplom.e_commerce.entities.Address;
import dyplom.e_commerce.entities.CartItem;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entities.Order;
import dyplom.e_commerce.entities.checkout.CheckoutInfo;
import dyplom.e_commerce.entityService.*;
import dyplom.e_commerce.repositories.CustomerRepository;
import dyplom.e_commerce.setting.EmailSettingBag;
import dyplom.e_commerce.util.Utility;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CustomerService customerService;
    private final AddressService addressService;
    private final ShoppingCartService shoppingCartService;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    private final SettingService settingService;

    public CheckoutController(CheckoutService checkoutService, CustomerService customerService, AddressService addressService, ShoppingCartService shoppingCartService, CustomerRepository customerRepository, OrderService orderService, SettingService settingService) {
        this.checkoutService = checkoutService;
        this.customerService = customerService;
        this.addressService = addressService;
        this.shoppingCartService = shoppingCartService;
        this.customerRepository = customerRepository;
        this.orderService = orderService;
        this.settingService = settingService;
    }

    @GetMapping("/app/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request) {
        var email = request.getUserPrincipal().getName();
        Customer customer = customerRepository.findByEmail(email);

        Address defaultAddress = addressService.getDefaultAddress(customer);

        if (defaultAddress != null) {
            model.addAttribute("shippingAddress", defaultAddress.toString());
        } else {
            model.addAttribute("shippingAddress", customer.getAddress());
        }
        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }

    @PostMapping("/app/place-order")
    public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        var email = request.getUserPrincipal().getName();
        Customer customer = customerRepository.findByEmail(email);

        Address defaultAddress = addressService.getDefaultAddress(customer);

        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

        Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, checkoutInfo);
        shoppingCartService.deleteByCustomer(customer);
        sendOrderConfirmation(request, createdOrder);
        return "checkout/order_completed";
    }

    private void sendOrderConfirmation(HttpServletRequest request, Order createdOrder) throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
        mailSender.setDefaultEncoding("utf-8");
        String toAddress = createdOrder.getCustomer().getEmail();
        String subject = emailSettingBag.getOrderConfirmationSubject();
        String content = emailSettingBag.getOrderConfirmationContent();

        subject = subject.replace("orderId", String.valueOf(createdOrder.getId()));

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
        String orderTime = dateFormat.format(createdOrder.getOrderTime());

        content = content.replace("name", createdOrder.getCustomerName() + "\n" +
                "- Order ID: " + String.valueOf(createdOrder.getId()) + "%n" +
                "- Order time: " + orderTime + "%n" +
                "- Shipping address: " + createdOrder.getShippingAddress() + "\n\n" +
                "- Total price: " + createdOrder.getTotal());

        helper.setText(content, true);
        mailSender.send(mimeMessage);

    }
}
