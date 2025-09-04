package pl.sebastianklimas.couponcalculator.models;

import java.math.BigDecimal;
import java.util.List;

public class FullShoppingListWithCoupon implements Comparable<FullShoppingListWithCoupon> {
    private List<BasketCoupon> basketCoupons;
    private BigDecimal totalPrice;

    public FullShoppingListWithCoupon(List<BasketCoupon> basketCoupons) {
        this.basketCoupons = basketCoupons;
    }

    public List<BasketCoupon> getBasketCoupons() {
        return basketCoupons;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void calculateTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BasketCoupon basketCoupon : basketCoupons) {
            sum = sum.add(basketCoupon.getFinalSum());
        }
        totalPrice = sum;
    }

    @Override
    public String toString() {
        return "FullShoppingListWithCoupon = { " + "basketCoupons = " + basketCoupons + " }";
    }

    @Override
    public int compareTo(FullShoppingListWithCoupon o) {
        return 0;
    }
}
