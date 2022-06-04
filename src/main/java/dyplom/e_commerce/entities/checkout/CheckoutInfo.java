package dyplom.e_commerce.entities.checkout;

public class CheckoutInfo {

    private float productCost;
    private float productTotal;

    public CheckoutInfo() {}

    public float getProductCost() {
        return productCost;
    }

    public void setProductCost(float productCost) {
        this.productCost = productCost;
    }

    public float getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(float productTotal) {
        this.productTotal = productTotal;
    }
}
