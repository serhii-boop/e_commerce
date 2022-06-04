package dyplom.e_commerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    @GetMapping("/admin-page/report")
    public String viewSalesReportHome() {
        return "reports/reports";
    }
}
