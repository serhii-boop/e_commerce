package dyplom.e_commerce.controllers.CategoryController;

import dyplom.e_commerce.entityService.CategoryService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin-page/categories/check")
    public String checkCategory(@Param("id") Integer id, @Param("category") String category) {
        return categoryService.isCategoryUnique(id, category) ? "Ok" : "Duplicated";
    }
}
