package dyplom.e_commerce.controllers.CategoryController;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.export.CategoryCsvExporter;
import dyplom.e_commerce.entities.export.CategoryPdfExport;
import dyplom.e_commerce.entityService.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin-page")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String listAll(Model model) {
        return categoryListByPage(1, model, "name", "asc", null);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String categoryListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                                 @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                                 @Param("keyword") String keyword){

        Page<Category> page = categoryService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Category> categoryList = page.getContent();
        long startCount = (long) (pageNum - 1) * CategoryService.CATEGORY_PER_PAGE + 1;
        long endCount = startCount * CategoryService.CATEGORY_PER_PAGE - 1;
        long totalElements = page.getTotalElements();
        if (endCount > totalElements) {
            endCount = totalElements;
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("listCategory",categoryList);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("totalElements", totalElements);

        return "admin_categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        Category category = new Category();
        category.setEnabled(true);
        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Create category");
        return "admin_categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute("category") Category category,
                               RedirectAttributes ra,
                               @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        category.setImage(fileName);

        Category savedCategory = categoryService.saveCategory(category);

        String uploadDir = "./category/" +  savedCategory.getId();

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

        ra.addFlashAttribute("message", "The category created successfully");
        return "redirect:/admin-page/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,
                           Model model){
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("pageTitle", "Edit category: " + category.getAlias() + ")");
        model.addAttribute("category", category);
        return "admin_categories/category_form";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("message", "The category deleted successfully");
        return "redirect:/admin-page/categories";
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryStatus(@PathVariable("id") int id,
                                   @PathVariable("status") boolean status,
                                   RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, status);
        String enab = status ? "enabled" : "disabled";
        String message = "The category with id " + id + " has been " + enab;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin-page/categories";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> categoryList = categoryService.categoryList();
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(categoryList, response);
    }

    @GetMapping("/categories/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<Category> categoryList = categoryService.categoryList();
        CategoryPdfExport exporter = new CategoryPdfExport();
        exporter.export(categoryList, response);
    }

}
