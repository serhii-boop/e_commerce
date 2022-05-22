package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.Product;
import dyplom.e_commerce.entities.Role;
import dyplom.e_commerce.entities.Status;
import dyplom.e_commerce.entities.User;
import dyplom.e_commerce.entityService.ProductService;
import dyplom.e_commerce.entityService.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/app")
public class AuthController {

    private final UserService userService;
    private final ProductService productService;

    public AuthController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }


    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process-register")
    public String processRegistration(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRegistrationDate(LocalDateTime.now());
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);
        userService.saveUser(user);

        return "index";
    }

    @GetMapping("/resistors")
    public String getResistorsList(Model model,
                                   @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size){
        return getResistorsListByPage(model, 1);
    }
    @GetMapping("/resistors/{pageNumber}")
    public String getResistorsListByPage(Model model,
                                   @PathVariable("pageNumber") int page){
        Page<Product> firstPage = productService.getResistorsList(page);
        List<Product> productList = firstPage.getContent();
        long totalItems = firstPage.getTotalElements();
        int totalPages = firstPage.getTotalPages();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("resistorList", productList);
        return "resistorList";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveStudent(@ModelAttribute("product") Product product,
                              RedirectAttributes ra,
                              @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        product.setCategory("resistor");
        product.setPhotoPath(fileName);
        Product savedProduct = productService.saveResistor(product);
        String uploadDir = "./logo/" + savedProduct.getId();

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not saved uploaded file");
        }

        ra.addFlashAttribute("message", "Item has been saved successfully");
        return "redirect:/app/resistors";
    }
    @RequestMapping("/create/{id}")
    public ModelAndView showEditResistorPage(@PathVariable (name="id") Integer pid) {
        ModelAndView mav=new ModelAndView("createResistor");
        Product product = productService.getProductById(pid);
        mav.addObject("resistor",product);
        return mav;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage() {
        return "index";
    }
}
