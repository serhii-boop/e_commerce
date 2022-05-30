package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.Address;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entityService.AddressService;
import dyplom.e_commerce.repositories.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AddressController {

    private final AddressService addressService;
    private final CustomerRepository customerRepository;

    public AddressController(AddressService addressService, CustomerRepository customerRepository) {
        this.addressService = addressService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/app/address")
    public String showAddressBook(Model model, HttpServletRequest request) {
        var email = request.getUserPrincipal().getName();
        Customer customer = customerRepository.findByEmail(email);
        List<Address> addressList = addressService.listAddressBook(customer);
        boolean usePrimary = true;
        for (Address address: addressList) {
            if (address.isDefaultForShipping()) {
                usePrimary = false;
                break;
            }
        }

        model.addAttribute("addressList", addressList);
        model.addAttribute("customer", customer);
        model.addAttribute("usePrimary", usePrimary);
        return "address/address_book";
    }

    @GetMapping("/app/address/new")
    public String newAddress(Model model) {

        model.addAttribute("address", new Address());
        model.addAttribute("pageTitle", "Add New Address");

        return "address/address_form";
    }

    @PostMapping("/app/address/save")
    public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) {
        var email = request.getUserPrincipal().getName();
        Customer customer = customerRepository.findByEmail(email);

        address.setCustomer(customer);
        addressService.save(address);

        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/app/address";

        if ("checkout".equals(redirectOption)) {
            redirectURL += "?redirect=checkout";
        }

        ra.addFlashAttribute("message", "The address has been saved successfully.");

        return redirectURL;
    }

    @GetMapping("/app/address/edit/{id}")
    public String editAddress(@PathVariable("id") Integer addressId, Model model,
                              HttpServletRequest request) {
        var email = request.getUserPrincipal().getName();
        Customer customer = customerRepository.findByEmail(email);

        Address address = addressService.getByAddressIdAndCustomerId(addressId, customer.getId());

        model.addAttribute("address", address);
        model.addAttribute("pageTitle", "Edit Address (ID: " + addressId + ")");

        return "address/address_form";
    }

    @GetMapping("/app/address/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes ra,
                                HttpServletRequest request) {
        var email = request.getUserPrincipal().getName();
        Customer customer = customerRepository.findByEmail(email);
        addressService.delete(addressId, customer.getId());

        ra.addFlashAttribute("message", "The address ID " + addressId + " has been deleted.");

        return "redirect:/app/address";
    }

    @GetMapping("/app/address/default/{id}")
    public String setDefaultAddress(@PathVariable("id") Integer addressId,
                                    HttpServletRequest request) {
        var email = request.getUserPrincipal().getName();
        Customer customer = customerRepository.findByEmail(email);
        addressService.setDefaultAddress(addressId, customer.getId());

        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/app/address";

        if ("cart".equals(redirectOption)) {
            redirectURL = "redirect:/app/cart";
        } else if ("checkout".equals(redirectOption)) {
            redirectURL = "redirect:/app/checkout";
        }

        return redirectURL;
    }
}
