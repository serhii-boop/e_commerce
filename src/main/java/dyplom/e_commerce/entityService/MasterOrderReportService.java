package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.Order;
import dyplom.e_commerce.entities.reports.AbstractReportService;
import dyplom.e_commerce.entities.reports.ReportItem;
import dyplom.e_commerce.entities.reports.ReportTime;
import dyplom.e_commerce.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MasterOrderReportService extends AbstractReportService {
    private final OrderRepository orderRepository;


    public MasterOrderReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    protected List<ReportItem> getReportDateByDateRangeInternal(Date startTime, Date endTime, ReportTime reportType) {
        List<Order> orderList = orderRepository.findByOrderTimeBetween(startTime, endTime);
        List<ReportItem> reportItemList = createReportData(startTime, endTime, reportType);
        calculateSalesForReportData(orderList, reportItemList);
        return reportItemList;
    }

    private List<ReportItem> createReportData(Date startTime, Date endTime, ReportTime reportType) {
        List<ReportItem> reportItemList = new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startTime);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endTime);
        Date currentDare = startDate.getTime();
        String dateString = dateFormat.format(currentDare);
        reportItemList.add(new ReportItem(dateString));
        do {
            if (reportType.equals(ReportTime.DAY)) {
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            } else if (reportType.equals(ReportTime.MONTH)) {
                startDate.add(Calendar.MONTH, 1);
            }

            currentDare = startDate.getTime();
            dateString = dateFormat.format(currentDare);
            reportItemList.add(new ReportItem(dateString));

        } while (startDate.before(endDate));
        return reportItemList;
    }

    private void calculateSalesForReportData(List<Order> orderList, List<ReportItem> reportItemList) {
        for (Order order: orderList) {
            String orderDateString = dateFormat.format(order.getOrderTime());

            ReportItem reportItem = new ReportItem(orderDateString);
            int itemIndex = reportItemList.indexOf(reportItem);
            if (itemIndex >= 0) {
                reportItem = reportItemList.get(itemIndex);
                reportItem.addSales(order.getTotal());
                reportItem.increaseOrderCount();
            }
        }
    }

}
