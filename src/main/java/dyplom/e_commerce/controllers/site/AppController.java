package dyplom.e_commerce.controllers.site;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entityService.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/app")
public class AppController {

    private final CategoryService categoryService;

    public AppController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String viewHomePage(Model model) {
        List<Category> categoryList = categoryService.categoryList();
        model.addAttribute("listCategory", categoryList);
        return "app/indexHome";
    }

}
