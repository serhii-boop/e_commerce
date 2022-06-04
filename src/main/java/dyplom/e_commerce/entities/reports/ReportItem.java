package dyplom.e_commerce.entities.reports;

import java.util.Objects;

public class ReportItem {

    private String ident;
    private float sales;
    private int ordersCount;

    public ReportItem() {}

    public ReportItem(String ident) {
        this.ident = ident;
    }

    public ReportItem(String ident, float sales) {
        this.ident = ident;
        this.sales = sales;
    }


    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public float getSales() {
        return sales;
    }

    public void setSales(float sales) {
        this.sales = sales;
    }

    public int getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(int ordersCount) {
        this.ordersCount = ordersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportItem that = (ReportItem) o;
        return Float.compare(that.sales, sales) == 0 && ordersCount == that.ordersCount && Objects.equals(ident, that.ident);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ident, sales, ordersCount);
    }

    public void addSales(float total) {
        this.sales += total;
    }

    public void increaseOrderCount() {
        this.ordersCount++;
    }
}
