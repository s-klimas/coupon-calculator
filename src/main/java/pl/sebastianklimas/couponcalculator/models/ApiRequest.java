package pl.sebastianklimas.couponcalculator.models;

import lombok.Getter;

import java.util.List;

@Getter
public class ApiRequest {
    private List<Product> products;
    private List<Coupon> coupons;
}
