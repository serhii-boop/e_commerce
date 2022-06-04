package dyplom.e_commerce.entities.reports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService {

    protected DateFormat dateFormat;
    public List<ReportItem> getReportDataLast7Days(ReportTime reportType) {
        return getReportDataLastXDays(7, reportType);
    }

    public List<ReportItem> getReportDataLast30Days(ReportTime reportType) {
        return getReportDataLastXDays(30, reportType);
    }

    protected List<ReportItem> getReportDataLastXDays(int days, ReportTime reportType) {
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, - (days - 1));
        Date startTime = calendar.getTime();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDateByDateRangeInternal(startTime, endTime, reportType);
    }

    public List<ReportItem> getReportDataLast180Days(ReportTime reportType) {
        return getReportDataLastXMonth(6, reportType);
    }

    public List<ReportItem> getReportDataLast365Days(ReportTime reportType) {
        return getReportDataLastXMonth(12, reportType);
    }

    public List<ReportItem> getReportDataLastXMonth(int month, ReportTime reportType) {
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, - (month - 1));
        Date startTime = calendar.getTime();
        dateFormat = new SimpleDateFormat("yyyy-MM");
        return getReportDateByDateRangeInternal(startTime, endTime, reportType);
    }

    public List<ReportItem> getReportDateByDateRange(Date startTime, Date endTime, ReportTime reportType) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDateByDateRangeInternal(startTime, endTime, reportType);
    }

    protected abstract List<ReportItem> getReportDateByDateRangeInternal(Date startDate, Date endDate, ReportTime reportType);

}
