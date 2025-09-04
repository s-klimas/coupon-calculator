package pl.sebastianklimas.couponcalculator.models;

import java.util.List;

public class ApiRequest {
    private List<Product> products;
    private List<Coupon> coupons;

    public List<Product> getProducts() {
        return products;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
