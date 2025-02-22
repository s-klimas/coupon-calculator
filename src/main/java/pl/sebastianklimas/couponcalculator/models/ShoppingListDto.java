package pl.sebastianklimas.couponcalculator.models;

import pl.sebastianklimas.couponcalculator.models.dtos.CouponDto;
import pl.sebastianklimas.couponcalculator.models.dtos.ProductDto;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListDto {
    private List<ProductDto> products = new ArrayList<>();
    private List<CouponDto> coupons = new ArrayList<>();

    public ShoppingListDto() {
    }

    public ShoppingListDto(List<ProductDto> products, List<CouponDto> coupons) {
        this.products = products;
        this.coupons = coupons;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public List<CouponDto> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponDto> coupons) {
        this.coupons = coupons;
    }
}
