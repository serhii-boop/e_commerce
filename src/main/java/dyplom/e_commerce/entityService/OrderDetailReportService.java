package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.OrderDetail;
import dyplom.e_commerce.entities.reports.AbstractReportService;
import dyplom.e_commerce.entities.reports.ReportItem;
import dyplom.e_commerce.entities.reports.ReportTime;
import dyplom.e_commerce.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailReportService extends AbstractReportService {

    @Autowired
    private OrderDetailRepository repo;


    @Override
    protected List<ReportItem> getReportDateByDateRangeInternal(Date startDate, Date endDate, ReportTime reportType) {
        List<OrderDetail> listOrderDetails = null;

        if (reportType.equals(ReportTime.CATEGORY)) {
            listOrderDetails = repo.findByCategoryAndTimeBetween(startDate, endDate);
        } else if (reportType.equals(ReportTime.PRODUCT)) {
            listOrderDetails = repo.findByProductAndTimeBetween(startDate, endDate);
        }

        List<ReportItem> listReportItems = new ArrayList<>();

        for (OrderDetail detail : listOrderDetails) {
            String identifier = "";

            if (reportType.equals(ReportTime.CATEGORY)) {
                identifier = detail.getProduct().getCategory().getName();
            } else if (reportType.equals(ReportTime.PRODUCT)) {
                identifier = detail.getProduct().getName();
            }

            float totalSales = detail.getSubtotal();

            if (listReportItems.isEmpty()) {
                listReportItems.add(new ReportItem(identifier, totalSales, detail.getQuantity()));
            }
            boolean flag = false;
            for (ReportItem reportItem1: listReportItems){
                if (reportItem1.getIdent().equals(identifier)) {
                    reportItem1.addSales(totalSales);
                    reportItem1.increaseProductsCount(detail.getQuantity());
                    flag = true;
                }
            }
             if (!flag) {
                 listReportItems.add(new ReportItem(identifier, totalSales, detail.getQuantity()));
             }

//            for (ReportItem reportItem1: listReportItems) {
//                if (reportItem1.getIdent().equals(identifier)) {
//                    reportItem = listReportItems.get(itemIndex);
//                    reportItem.addSales(totalSales);
//                    reportItem.increaseProductsCount(detail.getQuantity());
//                } else {
//                    listReportItems.add(new ReportItem(identifier, totalSales, detail.getQuantity()));
//                }
//            }
//            System.out.println(itemIndex);
//            if (itemIndex >= 0) {
//                reportItem = listReportItems.get(itemIndex);
//                reportItem.addSales(totalSales);
//                reportItem.increaseProductsCount(detail.getQuantity());
//            } else {
//                System.out.println(identifier);
//                listReportItems.add(new ReportItem(identifier, totalSales, detail.getQuantity()));
//            }
        }

        //printReportData(listReportItems);

        return listReportItems;
    }

}
