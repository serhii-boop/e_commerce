package dyplom.e_commerce.controllers.site;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Product;
import dyplom.e_commerce.entityService.CategoryService;
import dyplom.e_commerce.entityService.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductAppController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductAppController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword, Model model){
        return search(keyword, model, 1);
    }

    @GetMapping("/search/page/{pageNum}")
    public String search(@Param("keyword") String keyword, Model model, @PathVariable("pageNum") int pageNum) {
        Page<Product> productPage = productService.search(keyword, pageNum);
        List<Product> productList = productPage.getContent();

        long startCount = (long) (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount * ProductService.PRODUCT_PER_PAGE - 1;
        long totalElements = productPage.getTotalElements();
        if (endCount > totalElements) {
            endCount = totalElements;
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", keyword + " - Search result");
        model.addAttribute("productList", productList);
        return "app/search_result";
    }

    @GetMapping("/app/c/{category_name}")
    public String viewCategoryFirstPage(@PathVariable("category_name") String categoryName,
                                     Model model) {
        return viewCategoryByPage(categoryName, model, 1);
    }

    @GetMapping("/app/c/{category_name}/page/{page_num}")
    public String viewCategoryByPage(@PathVariable("category_name") String categoryName,
                               Model model, @PathVariable("page_num") int pageNum) {
        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            return "error/404";
        }
        Page<Product> productPage = productService.productListByCategory(1,category.getId());
        List<Product> productList = productPage.getContent();
        long startCount = (long) (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount * ProductService.PRODUCT_PER_PAGE - 1;
        long totalElements = productPage.getTotalElements();
        if (endCount > totalElements) {
            endCount = totalElements;
        }

        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("listCategory", categoryService.categoryList());
        return "app/products_by_category";
    }
}
