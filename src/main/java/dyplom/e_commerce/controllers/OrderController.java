package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.Customer;
import dyplom.e_commerce.entities.Order;
import dyplom.e_commerce.entityService.CustomerService;
import dyplom.e_commerce.entityService.OrderService;
import dyplom.e_commerce.entityService.SettingService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final SettingService settingService;

    public OrderController(OrderService orderService, SettingService settingService) {
        this.orderService = orderService;
        this.settingService = settingService;
    }

    @GetMapping("/admin-page/order")
    public String listFirstPage(Model model) {
        return listByPage(model, 1, "firstName", "asc", null);
    }

    @GetMapping("/admin-page/order/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {

        Page<Order> page = orderService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Order> orderList = page.getContent();
        long startCount = (long) (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
        long endCount = startCount * OrderService.ORDERS_PER_PAGE - 1;
        long totalElements = page.getTotalElements();
        if (endCount > totalElements) {
            endCount = totalElements;
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("orderList", orderList);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("totalElements", totalElements);

        return "admin_orders/orders";
    }

    @GetMapping("/admin-page/order/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes ra) {

        Order order = orderService.getById(id);
        model.addAttribute("order", order);

        return "admin_orders/order_detail";
    }

    @GetMapping("/admin-page/order/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            orderService.delete(id);;
            ra.addFlashAttribute("message", "The order ID " + id + " has been deleted.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/admin-page/order";
    }

}
