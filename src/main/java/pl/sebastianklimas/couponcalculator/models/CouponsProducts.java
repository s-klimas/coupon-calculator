package pl.sebastianklimas.couponcalculator.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CouponsProducts {
    private List<Map<Coupon, Set<Product>>> listOfMappedCouponWithProducts = new ArrayList<>();

    public CouponsProducts() {
    }

    public List<Map<Coupon, Set<Product>>> getListOfMappedCouponWithProducts() {
        return listOfMappedCouponWithProducts;
    }

    public void setListOfMappedCouponWithProducts(List<Map<Coupon, Set<Product>>> listOfMappedCouponWithProducts) {
        this.listOfMappedCouponWithProducts = listOfMappedCouponWithProducts;
    }

    public void addEntryToMap(Coupon coupon, Set<Product> products) {
        listOfMappedCouponWithProducts.add(Map.of(coupon, products));
    }
}
