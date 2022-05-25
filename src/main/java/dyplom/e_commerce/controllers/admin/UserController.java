package dyplom.e_commerce.controllers.admin;

import dyplom.e_commerce.entities.User;
import dyplom.e_commerce.entities.export.UserCsvExporter;
import dyplom.e_commerce.entities.export.UserPdfExporter;
import dyplom.e_commerce.entityService.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin-page")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        return userListByPage(1, model, "lastName", "asc", null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String userListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                                 @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                                 @Param("keyword") String keyword){

        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> userList = page.getContent();
        long startCount = (long) (pageNum - 1) * UserService.USER_PER_PAGE + 1;
        long endCount = startCount * UserService.USER_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("listUser",userList);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("totalElements", page.getTotalElements());

        return "admin_user/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create user");
        model.addAttribute("roleList", userService.roleList());
        return "admin_user/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "The user created successfully");
        return "redirect:/admin-page/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model){
        User user = userService.getUserById(id);
        model.addAttribute("pageTitle", "Edit user (Email: " + user.getEmail() + ")");
        model.addAttribute("user", user);
        model.addAttribute("roleList", userService.roleList());
        return "admin_user/user_form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        userService.delete(id);
        model.addAttribute("roleList", userService.roleList());
        redirectAttributes.addFlashAttribute("message", "The user deleted successfully");
        return "redirect:/admin-page/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserStatus(@PathVariable("id") int id,
                                   @PathVariable("status") boolean status,
                                    RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, status);
        String enab = status ? "enabled" : "disabled";
        String message = "The user with id " + id + " has been " + enab;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin-page/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> userList = userService.getAll();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(userList, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<User> userList = userService.getAll();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(userList, response);
    }
}
