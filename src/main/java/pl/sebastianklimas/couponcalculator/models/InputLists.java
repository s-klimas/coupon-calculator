package pl.sebastianklimas.couponcalculator.models;

import java.util.List;

public class InputLists {
    private List<Product> products;
    private List<Coupon> coupons;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}
