package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entities.export.CategoryCsvExporter;
import dyplom.e_commerce.entities.export.CategoryPdfExport;
import dyplom.e_commerce.entities.export.CustomerCsvExporter;
import dyplom.e_commerce.entities.export.CustomerPdfExporter;
import dyplom.e_commerce.entityService.CustomerService;
import dyplom.e_commerce.entityService.SettingService;
import dyplom.e_commerce.repositories.CustomerRepository;
import dyplom.e_commerce.setting.EmailSettingBag;
import dyplom.e_commerce.util.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final SettingService settingService;

    public CustomerController(CustomerService customerService, CustomerRepository customerRepository, SettingService settingService) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.settingService = settingService;
    }

    @GetMapping("/app/register")
    public String registerForm(Model model) {

        model.addAttribute("pageTitle", "Customer registration");
        model.addAttribute("customer", new Customer());
        return "register/register_form";
    }

    @PostMapping("/app/create_customer")
    public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);
        redirectAttributes.addFlashAttribute("message", "The user created successfully");
        return "register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
        String toAddress = customer.getEmail();
        String subject = emailSettingBag.getCustomerVerifySubject();
        String content = emailSettingBag.getCustomerVerifyContent();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String fullName= customer.getFirstName() + " " + customer.getLastName();
        content = content.replace("[[name]]", fullName);

        String verifyUrl = Utility.getSiteURL(request) + "/app/verify?code=" + customer.getVerificationCode();

        content = content.replace("URL", verifyUrl);

        helper.setText(content, true);
        mailSender.send(mimeMessage);
        System.out.println("to address" + toAddress);
        System.out.println(verifyUrl);
    }

    @GetMapping("/app/verify")
    public String verifyAccount(@Param("code") String code, Model model) {
        boolean verified = customerService.verify(code);

        return "register/" + (verified ? "verify_success" : "verify_fail");
    }

    @GetMapping("/admin-page/customers")
    public String listFirstPage(Model model) {
        return listByPage(model, 1, "firstName", "asc", null);
    }

    @GetMapping("/admin-page/customers/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {

        Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Customer> customerList = page.getContent();
        long startCount = (long) (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
        long endCount = startCount * CustomerService.CUSTOMERS_PER_PAGE - 1;
        long totalElements = page.getTotalElements();
        if (endCount > totalElements) {
            endCount = totalElements;
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("customerList", customerList);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("totalElements", totalElements);

        return "admin_customers/customers";
    }

    @GetMapping("/admin-page/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        customerService.updateCustomerEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Customer ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin-page/customers";
    }

    @GetMapping("/admin-page/customers/detail/{id}")
    public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);

        return "admin_customers/customer_detail_modal";
    }

    @GetMapping("/admin-page/customers/edit/{id}")
    public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {

        Customer customer = customerService.getCustomerById(id);

        model.addAttribute("customer", customer);
        model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));

        return "admin_customers/customer_form";
    }

    @PostMapping("/admin-page/customers/save")
    public String saveCustomer(Customer customer, RedirectAttributes ra) {
        customerService.save(customer);
        ra.addFlashAttribute("message", "The customer ID " + customer.getId() + " has been updated successfully.");
        return "redirect:/admin-page/customers";
    }

    @GetMapping("/admin-page/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {

        customerService.delete(id);
        ra.addFlashAttribute("message", "The customer ID " + id + " has been deleted successfully.");

        return "redirect:/admin-page/customers";
    }

    @GetMapping("/admin-page/customers/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Customer> customerList = customerService.getAll();
        CustomerCsvExporter exporter = new CustomerCsvExporter();
        exporter.export(customerList, response);
    }

    @GetMapping("/admin-page/customers/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<Customer> customerList = customerService.getAll();
        CustomerPdfExporter exporter = new CustomerPdfExporter();
        exporter.export(customerList, response);
    }
}
