package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.reports.ReportItem;
import dyplom.e_commerce.entities.reports.ReportTime;
import dyplom.e_commerce.entityService.MasterOrderReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class ReportRestController {

    private final MasterOrderReportService masterOrderReportService;

    public ReportRestController(MasterOrderReportService masterOrderReportService) {
        this.masterOrderReportService = masterOrderReportService;
    }

    @GetMapping("/admin-page/report/sales-by-date/{period}")
    public List<ReportItem> getReportDateByDatePeriod(@PathVariable("period") String period) {

        switch (period) {
            case "last_7_days":
                return masterOrderReportService.getReportDataLast7Days(ReportTime.DAY);
            case "last_month":
                return masterOrderReportService.getReportDataLast30Days(ReportTime.DAY);
            case "last_6_month":
                return masterOrderReportService.getReportDataLast180Days(ReportTime.MONTH);
            case "last_year":
                return masterOrderReportService.getReportDataLast365Days(ReportTime.MONTH);
            default:
                return masterOrderReportService.getReportDataLast7Days(ReportTime.DAY);
        }
    }

    @GetMapping("/admin-page/report/sales-by-date/{startDate}/{endDate}")
    public List<ReportItem> getReportDateByDatePeriod(@PathVariable("startDate") String startDate,
                                                      @PathVariable("endDate") String endDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormat.parse(startDate);
        Date endTime = dateFormat.parse(endDate);
        return masterOrderReportService.getReportDateByDateRange(startTime, endTime, ReportTime.DAY);
    }
}
