package dyplom.e_commerce.controllers.productController;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Product;
import dyplom.e_commerce.entityService.CategoryService;
import dyplom.e_commerce.entityService.ProductService;
import dyplom.e_commerce.security.AppUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin-page/products")
    public String listAll(Model model) {
        return productListByPage(1, model, "name", "asc", null, 0);
    }

    @GetMapping("/admin-page/products/page/{pageNum}")
    public String productListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                                     @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                                     @Param("keyword") String keyword,
                                    @Param("categoryId") Integer categoryId){
        Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
        List<Product> productList = page.getContent();
        List<Category> categoryList = categoryService.categoryList();

        long startCount = (long) (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount * ProductService.PRODUCT_PER_PAGE - 1;
        long totalElements = page.getTotalElements();
        if (endCount > totalElements) {
            endCount = totalElements;
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProduct",productList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "admin_product/products";
    }

    @GetMapping("/app/p/{id}")
    public String getProductById(@PathVariable("id") int id, Model model) {
        Product product = productService.getById(id);
        List<Category> categoryList = categoryService.categoryList();
        model.addAttribute("product", product);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("pageTitle", product.getCategory().getName());
        return "app/product_detail";
    }

    @GetMapping("/admin-page/products/new")
    public String newProduct(Model model) {
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);
        List<Category> categoryList = categoryService.categoryList();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Create new Product");
        return "admin_product/product_form";
    }

    @PostMapping("/admin-page/products/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              RedirectAttributes ra,
                              @RequestParam("fileImage") MultipartFile multipartFile,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @AuthenticationPrincipal AppUserDetails loggedUser) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        product.setImage(fileName);
        setProductDetails(detailNames, detailValues, product);
        Product savedProduct = productService.save(product);

        String uploadDir = "./product/" +  productService.save(product).getId();

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

        ra.addFlashAttribute("message", "The product has been saved successfully");
        return "redirect:/admin-page/products";
    }

    private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;
        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }
    }

    @GetMapping("/admin-page/products/{id}/enabled/{status}")
    public String updateProductStatus(@PathVariable("id") int id,
                                       @PathVariable("status") boolean status,
                                       RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, status);
        String enab = status ? "enabled" : "disabled";
        String message = "The product with id " + id + " has been " + enab;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin-page/products";
    }

    @GetMapping("/admin-page/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("message", "The product deleted successfully");
        return "redirect:/admin-page/products";
    }

    @GetMapping("/admin-page/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Product product = productService.getById(id);
        List<Category> categoryList = categoryService.categoryList();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Edit product");
        redirectAttributes.addFlashAttribute("message", "The product deleted successfully");
        return "admin_product/product_form";
    }

    @GetMapping("/admin-page/products/detail/{id}")
    public String viewProductDetails(@PathVariable(name = "id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        redirectAttributes.addFlashAttribute("message", "The product deleted successfully");
        return "admin_product/product_detail_modal";
    }
}
