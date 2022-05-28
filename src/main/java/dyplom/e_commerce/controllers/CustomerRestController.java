package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entityService.CustomerService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/app/customer/check_email_unique")
    public String checkDuplicateEmail(@Param("email") String email){
        return customerService.isEmailUnique(email) ? "OK" : "Duplicated";
    }
}
